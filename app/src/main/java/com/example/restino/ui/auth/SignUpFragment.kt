package com.example.restino.ui.auth

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.restino.R
import com.example.restino.data.Result
import com.example.restino.data.model.UserAuth
import com.example.restino.data.repository.RestinoRepository
import com.example.restino.databinding.FragmentSignUpBinding
import com.example.restino.ui.MainActivity
import com.example.restino.util.*
import com.poovam.pinedittextfield.PinField.OnTextCompleteListener
import kotlinx.android.synthetic.main.layout_pin_entry.*
import kotlinx.android.synthetic.main.layout_pin_entry.view.*
import org.jetbrains.annotations.NotNull
import www.sanju.motiontoast.MotionToast


class SignUpFragment : Fragment() {


    private lateinit var binding: FragmentSignUpBinding
    private val TAG = "SignUpFragmet"

    private lateinit var viewModel: AuthViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        CurrentFragment.curr = Constance.SIGNUP
        (activity as AppCompatActivity).supportActionBar?.hide()

        val restinoRepository = RestinoRepository()

        val viewAuthModelProviderFactory = InjectorUtil.AuthViewModelProviderFactory(
            requireActivity().application,
            restinoRepository
        )
        viewModel =
            ViewModelProvider(this, viewAuthModelProviderFactory).get(AuthViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fbBack.setOnClickListener {
            findNavController().navigateUp()
        }
        setupNavigation()

        submitSignUp()
    }


    private fun submitSignUp() {
        binding.btnSubmitSignup.setOnClickListener {
            if (!(activity as MainActivity).isConnected)
                return@setOnClickListener

            if (!binding.etPhoneNumSignup.text.toString().isEmpty()) {
                if (binding.etPhoneNumSignup.text.toString().trim().length != 11 ||
                    binding.etPhoneNumSignup.text.toString().trim().substring(0, 2) != "09"
                ) {
                    binding.layPhoneNumSignup.error = "شماره موبایل وارد شده صحیح نیست"
                    return@setOnClickListener
                } else {
                    binding.layPhoneNumSignup.isErrorEnabled = false

                }

            } else {
                binding.layPhoneNumSignup.error = "شماره موبایل رو وارد کنید"
                return@setOnClickListener
            }
            if (!binding.etFirstName.text.toString().isEmpty()) {
                binding.layFirstName.isErrorEnabled = false
            } else {
                binding.layFirstName.error = "نام خود را وارد کنید"
                return@setOnClickListener
            }
            if (!binding.etLastName.text.toString().isEmpty()) {
                binding.layLastName.isErrorEnabled = false
            } else {
                binding.layLastName.error = "نام خانوادگی خود را وارد کنید"
                return@setOnClickListener
            }
            if (!binding.etNationalCode.text.toString().isEmpty()) {
                if (binding.etNationalCode.text.toString().trim().length != 10
                ) {
                    binding.layNationalCode.error = "کد ملی وارد شده صحیح نیست"
                    return@setOnClickListener
                } else {
                    binding.layNationalCode.isErrorEnabled = false

                }

            } else {
                binding.layNationalCode.error = "کد ملی خود را وارد کنید"
                return@setOnClickListener
            }



            if (!binding.etPasswordSignup.text.toString().isEmpty()) {
                if (!binding.etPasswordSignup.text.toString().trim().isValidPassword()) {
                    binding.layPasswordSignup.error =
                        "رمز عبور باید حداقل ۸ کاراکتر و شامل  یک حرف بزرگ و یک عدد باشد"
                    return@setOnClickListener
                } else {
                    binding.layPasswordSignup.isErrorEnabled = false

                }

            } else {
                binding.layPasswordSignup.error = "رمز عبور خود را وارد کنید"
                return@setOnClickListener
            }


            if (!binding.etConfirmPassword.text.toString().isEmpty()) {
                if (binding.etPasswordSignup.text.toString()
                        .trim() != binding.etConfirmPassword.text.toString().trim()
                ) {
                    binding.layConfirmPassword.error = "رمز عبور با تکرار آن باید برابر باشد"
                    return@setOnClickListener
                } else {
                    binding.layConfirmPassword.isErrorEnabled = false

                }

            } else {
                binding.layConfirmPassword.error = " تکرار رمز عبور خود را وارد کنید"
                return@setOnClickListener
            }


            //Toast.makeText(context, "طلاعات صحیح است", Toast.LENGTH_SHORT).show()

            val username = binding.etPhoneNumSignup.text.toString().trim()
            val password = binding.etPasswordSignup.text.toString().trim()
            val email = ""
            val firstName = binding.etFirstName.text.toString().trim()
            val lastName = binding.etLastName.text.toString().trim()
            val nationalCode = binding.etNationalCode.text.toString().trim()


            register(username, password, email, firstName, lastName, nationalCode)


        }

    }

    private fun register(
        username: String,
        password: String,
        email: String,
        first_name: String,
        last_name: String,
        national_code: String
    ) {


        viewModel.registerUser(username, password, email, first_name, last_name, national_code)
        viewModel.register.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Result.Success -> {
                    binding.pbRegister.hide()
                    response.data?.let {
                        Log.d(TAG, "register:Success ${response.data?.status}")

                        showCodeDialog(
                            UserAuth(
                                username = username,
                                password = password,
                                email = email,
                                first_name = first_name,
                                last_name = last_name,
                                national_code = national_code
                            )
                        )

                    }
                }
                is Result.Error -> {
                    binding.pbRegister.hide()
                    Log.d(TAG, "register:Error ${response.data?.status}")
                    MotionToast.createToast(
                        requireActivity(), "کاربری با این شماره همراه وجود دارد",
                        MotionToast.TOAST_ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(requireContext(), R.font.helvetica_regular)
                    )
                }
                is Result.Loading ->
                    binding.pbRegister.show()


            }

        })
    }


    private fun showCodeDialog(userAuth: UserAuth?) {
        val dialog = MaterialDialog(requireContext())
            .show {
                customView(R.layout.layout_pin_entry)
                cancelable(false)
                cancelOnTouchOutside(false)
            }

        val number = userAuth!!.username.NumberEnToFarsi()
        val str1="لطفا کد ارسال شده به "
        val str2="را وارد کنید"
        val ftext="$str1 $number $str2"
        dialog.tv_number.text = ftext
        dialog.lineField.requestFocus()

        dialog.btn_confirm_code.setOnClickListener {
            if (!(activity as MainActivity).isConnected)
                return@setOnClickListener

            if (dialog.view.lineField.text.toString().length < 5) {
                dialog.btn_confirm_code.isEnabled = false
                return@setOnClickListener
            }
//            Toast.makeText(context, dialog.view.lineField.text.toString(), Toast.LENGTH_SHORT)
//                .show()

            setUpActiveReq(dialog, userAuth!!)

            dismissKeyboard()
        }
        dialog.view.lineField.onTextCompleteListener = object : OnTextCompleteListener {
            override fun onTextComplete(@NotNull enteredText: String): Boolean {
                dialog.btn_confirm_code.isEnabled = true
                return true // Return true to keep the keyboard open else return false to close the keyboard
            }
        }
        dialog.btn_cancle_code.setOnClickListener {
            dismissKeyboard()
            dialog.view.lineField.text?.clear()
            dialog.dismiss()
        }
    }

    private fun setUpActiveReq(
        dialog: MaterialDialog,
        userAuth: UserAuth
    ) {

        viewModel.activeUser(
            opt_code = dialog.view.lineField.text.toString(),
            username = userAuth.username,
            email = userAuth.email,
            password = userAuth.password,
            first_name = userAuth.first_name,
            last_name = userAuth.last_name,
            national_code = userAuth.national_code
        )
        viewModel.active.observe(viewLifecycleOwner, androidx.lifecycle.Observer { response ->
            when (response) {
                is Result.Success -> {
                    dialog.pb_active_code.hide()
                    dialog.tv_error_code.visibility = View.GONE
                    response.data?.let {
                        loginUser(userAuth)
                        dialog.dismiss()
                    }
                }
                is Result.Error -> {
//                    binding.pbHome.hide()
                    dialog.pb_active_code.hide()
                    dialog.tv_error_code.visibility = View.VISIBLE
                    dialog.lineField.text?.clear()
//                    response.message?.let {
//                        Log.e("HOMEFRAGMENT", "An error occured: $it")
//                    }
                }
                is Result.Loading -> {
                    dialog.pb_active_code.show()
                    dialog.tv_error_code.visibility = View.GONE
                }
                // binding.pbHome.show()


            }
        })
    }

    private fun loginUser(userAuth: UserAuth) {
        viewModel.loginUser(username = userAuth.username,
        password = userAuth.password)

        viewModel.login.observe(viewLifecycleOwner, Observer {response->
            when (response) {
                is Result.Success -> {
                    binding.pbRegister.hide()
                    response.data?.let {
                        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return@let
                        with (sharedPref.edit()) {
                            putString("accessToken", it.access)
                            putString("refreshToken", it.refresh)
                            commit()
                        }

                        (activity as MainActivity).UserIsLoggedIn()
                        findNavController().navigate(
                            SignUpFragmentDirections.actionSignUpFragmentToHomeFragment()

                        )
                    }
                }
                is Result.Error -> {
//                    binding.pbHome.hide()
                    binding.pbRegister.hide()

//                    response.message?.let {
//                        Log.e("HOMEFRAGMENT", "An error occured: $it")
//                    }
                }
                is Result.Loading -> {
                    binding.pbRegister.show()

                }
                // binding.pbHome.show()


            }
        })

    }


    private fun setupNavigation() {
        binding.homeButtonSignup.setOnClickListener {
            findNavController().navigate(
                SignUpFragmentDirections.actionSignUpFragmentToHomeFragment()
            )
        }
        binding.logInButtonSignup.setOnClickListener {
            findNavController().navigateUp()

        }
    }


}
