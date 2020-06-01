package com.example.restino.ui.auth

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController

import com.example.restino.R
import com.example.restino.databinding.FragmentLoginBinding
import com.example.restino.util.Constance
import com.example.restino.util.CurrentFragment

class LoginFragment : Fragment() {


    private lateinit var viewModel: AuthViewModel
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        CurrentFragment.curr=Constance.Login
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_login, container, false)
        binding.lifecycleOwner=this

        (activity as AppCompatActivity).supportActionBar?.hide()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        // TODO: Use the ViewModel

        binding.fbBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnSubmitLogin.setOnClickListener {

            Toast.makeText(context, "submit", Toast.LENGTH_SHORT).show()



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

}
