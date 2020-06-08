package com.example.restino.ui.profile.pro

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.restino.R
import com.example.restino.data.Result
import com.example.restino.data.remote.responceLocations.ResponceLocationsItem
import com.example.restino.data.repository.RestinoRepository
import com.example.restino.databinding.FragmentProfileBinding
import com.example.restino.ui.profile.loc.LocationsAdapter
import com.example.restino.util.*
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.layout_create_address.*
import www.sanju.motiontoast.MotionToast

private const val TAG = "ProfileFragment"

class ProfileFragment : Fragment(), LocationsAdapter.Interaction {


    private lateinit var viewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileBinding
    private lateinit var access: String
    private lateinit var locaAdapter: LocationsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CurrentFragment.curr = Constance.PROFILE
        (activity as AppCompatActivity).supportActionBar?.show()

        val restinoRepository = RestinoRepository()
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        access = sharedPref.getString("accessToken", "").toString()
        access = "Bearer $access"
        val viewProfileModelProviderFactory = InjectorUtil.ProfileViewModelProviderFactory(
            requireActivity().application,
            restinoRepository,
            access
        )
        viewModel =
            ViewModelProvider(
                this,
                viewProfileModelProviderFactory
            ).get(ProfileViewModel::class.java)

        subscribeUi()
        setupSwipeToRefresh()
        setupRvLocations()

        binding.fbBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnAddAddress.setOnClickListener {
            showCreateAddressDialog()
        }

    }

    private fun setupRvLocations() {
        binding.rvLocations.apply {
            layoutManager = LinearLayoutManager(context)
            locaAdapter = LocationsAdapter(this@ProfileFragment)
            adapter = locaAdapter
        }
        getLocations()
    }

    private fun getLocations() {
        viewModel.locations.observe(viewLifecycleOwner, Observer { responce ->
            when (responce) {
                is Result.Success -> {
                    binding.pbLocations.hide()
                    responce.data?.let {
                        if (it.isEmpty()){
                            binding.tvNoLoc.fadeShow()
                            binding.rvLocations.visibility=View.GONE
                        }else
                        {
                            binding.tvNoLoc.visibility=View.GONE
                            binding.rvLocations.fadeShow()
                        }
                        locaAdapter.submitList(it)

                    }
                }
                is Result.Error -> {
                    binding.pbLocations.hide()
                    responce.message?.let {
                        if (it == "not") {
                            getRefresh(binding.pbLocations, "locations", null)
                        }
                    }


                }
                is Result.Loading -> {
                    binding.pbLocations.show()


                }

            }

        })
    }

    private fun showCreateAddressDialog() {
        val dialogCreateAddress = MaterialDialog(requireContext())
            .show {
                customView(R.layout.layout_create_address)
                cancelable(false)
                cancelOnTouchOutside(false)
            }
        dialogCreateAddress.fb_back_dialog.setOnClickListener {
            dialogCreateAddress.dismiss()
        }
        dialogCreateAddress.btn_created_address.setOnClickListener {

            if (dialogCreateAddress.et_address.text.toString().isEmpty()) {
                dialogCreateAddress.lay_address.error = "آدرس را وارد کنید"
                return@setOnClickListener
            } else {
                dialogCreateAddress.lay_address.isErrorEnabled = false
            }
            if (!dialogCreateAddress.et_zip_code.text.toString().isEmpty()) {
                if (dialogCreateAddress.et_zip_code.text.toString().trim().length != 10) {
                    dialogCreateAddress.lay_zip_code.error = "کدپستی باید ۱۰ رقم باشد"
                    return@setOnClickListener
                } else {
                    dialogCreateAddress.lay_zip_code.isErrorEnabled = false
                }

            } else {
                dialogCreateAddress.lay_zip_code.error = "کد پستی را وارد کنید"
                return@setOnClickListener
            }


            if (dialogCreateAddress.et_plaque.text.toString().isEmpty()) {
                dialogCreateAddress.lay_plaque.error = "پلاک را وارد کنید"
                return@setOnClickListener
            } else {
                dialogCreateAddress.lay_plaque.isErrorEnabled = false
            }

            viewModel.createAddrees(
                access,
                address = dialogCreateAddress.et_address.text.toString(),
                zip_code = dialogCreateAddress.et_zip_code.text.toString(),
                plaque = dialogCreateAddress.et_plaque.text.toString(),
                lat = "",
                long = "",
                city = 1
            )

            dialogCreateAddress.dismiss()
            viewModel.getLocations(access)
        }

        viewModel.createAddress.observe(viewLifecycleOwner, Observer { responce ->
            when (responce) {
                is Result.Success -> {
                    dialogCreateAddress.pb_create_loc.hide()
                }
                is Result.Error -> {
                    dialogCreateAddress.pb_create_loc.hide()
                    responce.message?.let {
                        Log.d(TAG, "showCreateAddressDialog: $it")
                        if (it != "not") {
                            MotionToast.createToast(
                                requireActivity(), "مشکلی وجود دارد : ${it}",
                                MotionToast.TOAST_ERROR,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(requireContext(), R.font.helvetica_regular)
                            )
                        } else {
                            getRefresh(
                                dialogCreateAddress.pb_create_loc,
                                "createAddress",
                                dialogCreateAddress.btn_created_address
                            )
                        }
                    }

                }
                is Result.Loading -> {
                    dialogCreateAddress.pb_create_loc.show()

                }

            }
        })

    }

    private fun setupSwipeToRefresh() {
        binding.swipeView.setSwipeableChildren(binding.contScroll.id)
        binding.swipeView.setOnRefreshListener {

            viewModel.getProfile(access)
            viewModel.getLocations(access)
            Log.d(TAG, "setupSwipeToRefresh:\n$access ")
            binding.swipeView.isRefreshing = false
        }

    }

    private fun subscribeUi() {
        viewModel.profile.observe(viewLifecycleOwner, androidx.lifecycle.Observer { response ->
            when (response) {
                is Result.Success -> {
                    binding.pbProfile.hide()
                    response.data?.let {
                        // productRvAdapter.submitList(it)
                        //Toast.makeText(context, it.first_name, Toast.LENGTH_SHORT).show()
                        binding.tvName.text = "${it.first_name} ${it.last_name}"
                        binding.tvNumber.text = it.username.NumberEnToFarsi()
                        binding.tvEmail.text = " ایمیل :${it.email}"
                        binding.tvNationalCode.text =
                            "  کد ملی : ${it.national_code.NumberEnToFarsi()} "
                    }
                }
                is Result.Error -> {
                    binding.pbProfile.hide()
                    response.message?.let {
                        Log.d(TAG, "subscribeUi: $it")
                        if (it != "not") {
                            MotionToast.createToast(
                                requireActivity(), "مشکلی وجود دارد : ${it}",
                                MotionToast.TOAST_ERROR,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(requireContext(), R.font.helvetica_regular)
                            )
                        } else {
                            getRefresh(
                                binding.pbProfile,
                                "profile", null
                            )
                        }
                    }
                }
                is Result.Loading ->
                    binding.pbProfile.show()


            }
        })

    }

    private fun getRefresh(
        pb: ProgressBar,
        methodcall: String,
        btnCreatedAddress: MaterialButton?
    ) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val refresh = sharedPref.getString("refreshToken", "").toString()
        viewModel.getRefresh(refresh)
        viewModel.refreshToken.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Result.Success -> {
                    pb.hide()
                    response.data?.let {
                        with(sharedPref.edit()) {
                            putString("accessToken", it.access)
                            commit()
                        }
                        Log.d(TAG, "getRefresh:${it.access} ")
                        //TODO
                        access = it.access
                        when (methodcall) {
                            "profile" -> {
                                viewModel.getProfile("Bearer ${it.access}")
                            }
                            "createAddress" -> {
                                btnCreatedAddress?.callOnClick()
                            }
                            "locations" -> {
                                viewModel.getLocations("Bearer ${it.access}")
                            }
                            else -> {

                            }

                        }
                    }
                }
                is Result.Error -> {
                    pb.hide()

                }
                is Result.Loading -> {
                    pb.show()
                }
            }
        })

    }

    override fun onItemSelected(position: Int, item: ResponceLocationsItem) {
        Toast.makeText(context, "edit ${item.address}", Toast.LENGTH_SHORT).show()
    }

}