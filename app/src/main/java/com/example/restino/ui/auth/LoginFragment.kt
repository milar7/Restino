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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.restino.R
import com.example.restino.data.Result
import com.example.restino.data.repository.RestinoRepository
import com.example.restino.databinding.FragmentLoginBinding
import com.example.restino.ui.MainActivity
import com.example.restino.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import www.sanju.motiontoast.MotionToast

private const val TAG = "LoginFragment"

class LoginFragment : Fragment() {


    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        CurrentFragment.curr = Constance.Login
        (activity as AppCompatActivity).supportActionBar?.hide()

        val restinoRepository = RestinoRepository()

        val authModelProviderFactory = InjectorUtil.AuthViewModelProviderFactory(
            requireActivity().application,
            restinoRepository
        )
        viewModel =
            ViewModelProvider(this, authModelProviderFactory).get(AuthViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.lifecycleOwner = this




        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.fbBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnSubmitLogin.setOnClickListener {

            if(!(activity as MainActivity).isConnected)
                return@setOnClickListener


            if (!binding.etPhoneNumLogin.text.toString().trim().isEmpty()) {
                if (binding.etPhoneNumLogin.text.toString().trim().length!=11 ||
                    binding.etPhoneNumLogin.text.toString().trim().substring(0,2)!="09"){

                    binding.laPhoneNumLogin.error = "شماره موبایل وارد شده صحیح نیست"
                    return@setOnClickListener
                }else {
                    binding.laPhoneNumLogin.isErrorEnabled = false
                }
            }else{
                binding.laPhoneNumLogin.error = "شماره موبایل وارد را وارد کنید"
                return@setOnClickListener
            }

            if (binding.etPasswordLogin.text.toString().trim().isEmpty()) {
                binding.layPasswordLogin.error = "لطفا رمز عبور خود را را وارد کنید"
                return@setOnClickListener
            } else {
                binding.layPasswordLogin.isErrorEnabled = false
            }

            val username = binding.etPhoneNumLogin.text.toString().trim()
            val password = binding.etPasswordLogin.text.toString().trim()


            login(username, password)
            dismissKeyboard()
        }


        binding.homeButtonLogin.setOnClickListener {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToHomeFragment()
            )
        }
        binding.signupButtonLogin.setOnClickListener {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
            )
        }
    }

    private fun login(username: String, password: String) {
        viewModel.loginUser(username, password)

        viewModel.login.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Result.Success -> {
                    binding.pbLogin.hide()
                    response.data?.let {
                        Log.d(TAG, "login:Success =${it.access}")
                        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return@let
                        with (sharedPref.edit()) {
                            putString("accessToken", it.access)
                            putString("refreshToken", it.refresh)
                            commit()
                        }

                        (activity as MainActivity).UserIsLoggedIn()
                        MotionToast.createToast(
                            requireActivity(), "با موفقیت وارد شدید",
                            MotionToast.TOAST_SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(requireContext(), R.font.helvetica_regular)
                        )

                        lifecycleScope.launch {
                            delay(200L)
                            withContext(Dispatchers.Main){
                                if (findNavController().currentDestination?.id == R.id.loginFragment)
                                findNavController().navigate(
                                    LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                                )
                            }
                        }



                    }
                }
                is Result.Error -> {
                    binding.pbLogin.hide()
                    binding.layPasswordLogin.error="نام کاربری یا رمز عبور صحیح نمیباشد"
                    binding.laPhoneNumLogin.error="نام کاربری یا رمز عبور صحیح نمیباشد"
//                    MotionToast.createToast(
//                        requireActivity(), "نام کاربری یا رمز عبور صحیح نمیباشد",
//                        MotionToast.TOAST_ERROR,
//                        MotionToast.GRAVITY_BOTTOM,
//                        MotionToast.LONG_DURATION,
//                        ResourcesCompat.getFont(requireContext(), R.font.helvetica_regular)
//                    )
                }
                is Result.Loading ->
                    binding.pbLogin.show()


            }

        })

    }

}
