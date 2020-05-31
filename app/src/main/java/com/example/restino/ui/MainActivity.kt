package com.example.restino.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.example.restino.R
import com.example.restino.data.Result
import com.example.restino.ui.home.HomeFragmentDirections
import com.example.restino.ui.home.detail.DetailFragmentDirections
import com.example.restino.util.*
import com.example.restino.util.Constance
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        val actionBar = supportActionBar
        actionBar?.title = ""

        btn_sign_in_home.setOnClickListener {
            when (CurrentFragment.curr) {
                Constance.HOME -> {
                    Navigation.findNavController(this, R.id.nav_host_fragment)
                        .navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
                }
                Constance.Detail ->{
                    Navigation.findNavController(this, R.id.nav_host_fragment)
                        .navigate(DetailFragmentDirections.actionDetailFragmentToLoginFragment())
                }
                else ->{
                    Toast.makeText(this, "404", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

//TODO handle connectivity with sht like uncolor some where

//    override fun networkAvailable() {
//        super.networkAvailable()
//        tv.post {
//            with(tv){
//                text="connected"
//            }
//        }
//    }
//
//    override fun networkUnavailable() {
//        super.networkUnavailable()
//        tv.post {
//            with(tv){
//                text="connecting..."
//            }
//        }
//    }
}