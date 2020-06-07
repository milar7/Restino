package com.example.restino.ui

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.afollestad.materialdialogs.MaterialDialog
import com.example.restino.R
import com.example.restino.data.repository.RestinoRepository
import com.example.restino.ui.auth.AuthViewModel
import com.example.restino.ui.home.HomeFragmentDirections
import com.example.restino.ui.home.HomeViewModel
import com.example.restino.ui.home.detail.DetailFragmentDirections
import com.example.restino.ui.profile.pro.ProfileFragmentDirections
import com.example.restino.util.*
import com.example.restino.util.Constance
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MyMainActivity"
class MainActivity : BaseActivity() {

    lateinit var snackbarConnections:Snackbar
    lateinit var snackbarConnections2:Snackbar
     lateinit var viewModel:HomeViewModel
    var isConnected=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val restinoRepository = RestinoRepository()

        val viewModelProviderFactory = InjectorUtil.HomeViewModelProviderFactory(application,restinoRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(HomeViewModel::class.java)

        val sharedPref = this@MainActivity.getPreferences(Context.MODE_PRIVATE) ?: return
        val access = sharedPref.getString("accessToken", "")
        if (access!!.length>5)
        {
            UserIsLoggedIn()
        }else{
            btn_sign_in_home.fadeShow()
            lay_loggedIn.visibility=View.GONE
        }
        actionbarsetup()
        snackbarsetup()


        btn_log_out_home.setOnClickListener {
            if (isConnected)
                UserIsLoggedOut(object :DoYouWantoLoggOut{
                    override fun logout() {
                        btn_sign_in_home.fadeShow()
                        lay_loggedIn.visibility=View.GONE
                        val sharedPref = this@MainActivity.getPreferences(Context.MODE_PRIVATE) ?: return
                        with (sharedPref.edit()) {
                            putString("accessToken", "")
                            putString("refreshToken", "")
                            apply()
                        }

                        when (CurrentFragment.curr) {
                            Constance.HOME -> {
                            }
                            Constance.Detail ->{
                            }
                            Constance.PROFILE ->{
                                Navigation.findNavController(this@MainActivity, R.id.nav_host_fragment)
                                    .navigate(ProfileFragmentDirections.actionProfileFragmentToHomeFragment())
                            }
                            else ->{

                            }
                        }

                    }

                    override fun cancel() {
                    }

                })
            else
                Toast.makeText(this@MainActivity, "مشکل در ارتباط", Toast.LENGTH_SHORT).show()




        }

    }


    fun UserIsLoggedIn(){
        btn_sign_in_home.visibility=View.GONE
        lay_loggedIn.fadeShow()

    }

    fun UserIsLoggedOut(callback:DoYouWantoLoggOut){

        val dialog =MaterialDialog(this@MainActivity)
            .show {
                title(text = "خروج")
                message(text = "می‌خواهید از حساب کاربری خود خارج شوید؟")
                negativeButton(text = "خیر") {
                    callback.cancel()
                }
                positiveButton(text = "خروج") {
                    callback.logout()
                }
            }

    }


    interface DoYouWantoLoggOut {
        fun logout()
        fun cancel()
    }


    private fun actionbarsetup() {
        setSupportActionBar(findViewById(R.id.my_toolbar))
        val actionBar = supportActionBar
        actionBar?.title = ""

        btn_profile_home.setOnClickListener {
            when (CurrentFragment.curr) {
                Constance.HOME -> {
                    Navigation.findNavController(this, R.id.nav_host_fragment)
                        .navigate(HomeFragmentDirections.actionHomeFragmentToProfileFragment())
                }
                Constance.Detail ->{
                    Navigation.findNavController(this, R.id.nav_host_fragment)
                        .navigate(DetailFragmentDirections.actionDetailFragmentToProfileFragment())
                }
                Constance.PROFILE ->{
                    return@setOnClickListener
                }
                else ->{
                    Toast.makeText(this, "404", Toast.LENGTH_SHORT).show()
                }
            }
        }


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
                Constance.PROFILE ->{
                    return@setOnClickListener
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