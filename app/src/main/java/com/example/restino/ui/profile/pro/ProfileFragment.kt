package com.example.restino.ui.profile.pro

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.restino.R
import com.example.restino.data.Result
import com.example.restino.data.repository.RestinoRepository
import com.example.restino.databinding.FragmentProfileBinding
import com.example.restino.util.*
import www.sanju.motiontoast.MotionToast

private const val TAG = "ProfileFragment"
class ProfileFragment : Fragment() {



    private lateinit var viewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileBinding
    private lateinit var access:String

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
        access="Bearer $access"
        val viewProfileModelProviderFactory = InjectorUtil.ProfileViewModelProviderFactory(
            requireActivity().application,
            restinoRepository,
            access
        )
        viewModel =
            ViewModelProvider(this, viewProfileModelProviderFactory).get(ProfileViewModel::class.java)

        subscribeUi()
        setupSwipeToRefresh()

        binding.fbBack.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    private fun setupSwipeToRefresh() {
    binding.swipeView.setSwipeableChildren(binding.contScroll.id)
        binding.swipeView.setOnRefreshListener {

            viewModel.getProfile(access)
            Log.d(TAG, "setupSwipeToRefresh:\n$access ")
            binding.swipeView.isRefreshing=false
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
                        binding.tvName.text="${it.first_name} ${it.last_name}"
                        binding.tvNumber.text=it.username.NumberEnToFarsi()
                        binding.tvEmail.text = " ایمیل :${it.email}"
                        binding.tvNationalCode.text = "  کد ملی : ${it.national_code.NumberEnToFarsi()} "
                    }
                }
                is Result.Error -> {
                    binding.pbProfile.hide()
                    response.message?.let {
                        Log.d(TAG, "subscribeUi: $it")
                        MotionToast.createToast(
                            requireActivity(), "مشکلی وجود دارد : ${it}",
                            MotionToast.TOAST_ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(requireContext(), R.font.helvetica_regular)
                        )                    }
                }
                is Result.Loading ->
                    binding.pbProfile.show()


            }
        })

    }

}