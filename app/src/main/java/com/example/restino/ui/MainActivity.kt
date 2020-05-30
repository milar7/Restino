package com.example.restino.ui

import android.os.Bundle
import com.example.restino.R

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        val actionBar= supportActionBar
        actionBar?.title=""
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