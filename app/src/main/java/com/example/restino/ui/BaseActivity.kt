package com.example.restino.ui

import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import com.example.restino.util.NetworkStateReceiver

open class BaseActivity: AppCompatActivity() , NetworkStateReceiver.NetworkStateReceiverListener{
    lateinit var networkStateReceiver: NetworkStateReceiver

    override fun onStart() {
        super.onStart()
        networkStateReceiver = NetworkStateReceiver()
        networkStateReceiver.addListener(this)
        this.registerReceiver(networkStateReceiver, IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onDestroy() {
        super.onDestroy()
        this.unregisterReceiver(networkStateReceiver)
        networkStateReceiver.removeListener(this)
    }
    override fun networkAvailable() {

    }

    override fun networkUnavailable() {
    }

}