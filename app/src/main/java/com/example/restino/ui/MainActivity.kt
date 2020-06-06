package com.example.restino.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.restino.R
import com.example.restino.data.repository.RestinoRepository
import com.example.restino.ui.auth.AuthViewModel
import com.example.restino.ui.home.HomeFragmentDirections
import com.example.restino.ui.home.HomeViewModel
import com.example.restino.ui.home.detail.DetailFragmentDirections
import com.example.restino.util.*
import com.example.restino.util.Constance
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MyMainActivity"
class MainActivity : BaseActivity() {

    lateinit var snackbarConnections:Snackbar
    lateinit var snackbarConnections2:Snackbar
     lateinit var viewModel:HomeViewModel
     lateinit var viewAuthModel:AuthViewModel
    var isConnected=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val restinoRepository = RestinoRepository()

        val viewModelProviderFactory = InjectorUtil.HomeViewModelProviderFactory(application,restinoRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(HomeViewModel::class.java)




        val viewAuthModelProviderFactory = InjectorUtil.AuthViewModelProviderFactory(application,restinoRepository)
        viewAuthModel = ViewModelProvider(this, viewAuthModelProviderFactory).get(AuthViewModel::class.java)
        actionbarsetup()
        snackbarsetup()


    }

    private fun actionbarsetup() {
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

    private fun snackbarsetup() {
        snackbarConnections=Snackbar.make(container,"عدم اتصال به اینترنت ",Snackbar.LENGTH_INDEFINITE)
            .setCallback(object :Snackbar.Callback(){
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    if (event == DISMISS_EVENT_SWIPE && !isConnected)
                        snackbarConnections2.show()
                }
            })
            .setAction("تلاش دوباره", View.OnClickListener {
               // Toast.makeText(this, "در حال اتصال", Toast.LENGTH_SHORT).show()
                if (!isConnected){
                    snackbarConnections2.show()}
                else{
                    viewModel.getNewProducts()
                }
            })
            .setTextColor(resources.getColor(R.color.textRed))
        snackbarConnections2=Snackbar.make(container,"عدم اتصال به اینترنت ",Snackbar.LENGTH_INDEFINITE)
            .setCallback(object :Snackbar.Callback(){
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    if (event == DISMISS_EVENT_SWIPE && !isConnected)
                        snackbarConnections.show()
                }
            })
            .setTextColor(resources.getColor(R.color.textRed))
            .setAction("تلاش دوباره", View.OnClickListener {
                //Toast.makeText(this, "در حال اتصال", Toast.LENGTH_SHORT).show()
                if (!isConnected){
                    snackbarConnections.show()}
                else{
                    viewModel.getNewProducts()
                }
            })    }

//TODO handle connectivity with sht like uncolor some where

    override fun networkAvailable() {
        super.networkAvailable()
        Log.d(TAG, "networkAvailable")
        isConnected=true
      // snackbarConnections.dismiss()
       //snackbarConnections2.dismiss()
    }

    override fun networkUnavailable() {
        super.networkUnavailable()
        Log.d(TAG, "networkUnavailable")
        isConnected=false
       snackbarConnections.show()
    }
}