package com.example.restino.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.restino.databinding.FragmentSignUpBinding
import com.example.restino.R
import com.example.restino.util.Constance
import com.example.restino.util.CurrentFragment
import com.example.restino.util.isValidPassword
import com.example.restino.ui.auth.SignUpFragmentDirections


class SignUpFragment : Fragment() {


    private lateinit var binding: FragmentSignUpBinding
    private val TAG = "SignUpFragmet"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        CurrentFragment.curr= Constance.SIGNUP

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupNavigation()

        submitSignUp()
    }


    private fun submitSignUp() {
        binding.btnSubmitSignup.setOnClickListener {

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
                if ( !binding.etPasswordSignup.text.toString().trim().isValidPassword())
                {
                    binding.layPasswordSignup.error = "رمز عبور باید حداقل ۸ کاراکتر و شامل  یک حرف بزرگ و یک عدد باشد"
                    return@setOnClickListener
                } else {
                    binding.layPasswordSignup.isErrorEnabled = false

                }

            } else {
                binding.layPasswordSignup.error = "رمز عبور خود را وارد کنید"
                return@setOnClickListener
            }


            if (!binding.etConfirmPassword.text.toString().isEmpty()) {
                if (binding.etPasswordSignup.text.toString().trim()!=binding.etConfirmPassword.text.toString().trim())
                {
                    binding.layConfirmPassword.error = "رمز عبور با تکرار آن باید برابر باشد"
                    return@setOnClickListener
                } else {
                    binding.layConfirmPassword.isErrorEnabled = false

                }

            } else {
                binding.layConfirmPassword.error = " تکرار رمز عبور خود را وارد کنید"
                return@setOnClickListener
            }


            Toast.makeText(context, "طلاعات صحیح است", Toast.LENGTH_SHORT).show()

        }
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
