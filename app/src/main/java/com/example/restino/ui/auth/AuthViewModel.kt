package com.example.restino.ui.auth

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.restino.MyApplication
import com.example.restino.data.Result
import com.example.restino.data.remote.responceLogin.LoginResponse
import com.example.restino.data.remote.responceRegister.RegisterResponce
import com.example.restino.data.repository.RestinoRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class AuthViewModel(
    app: Application,
    val restinoRepository: RestinoRepository
) : AndroidViewModel(app) {


    val register: MutableLiveData<Result<RegisterResponce>> = MutableLiveData()
    val active: MutableLiveData<Result<RegisterResponce>> = MutableLiveData()
    val login: MutableLiveData<Result<LoginResponse>> = MutableLiveData()
    fun loginUser(username :String,
                  password :String
    )= viewModelScope.launch{
        safeLoginUser(username, password)
    }
    private suspend fun safeLoginUser(username: String, password: String) {
        login.postValue(Result.Loading())

        try {


            if (hasInternetConnection()){
                val responce=restinoRepository.loginUser(username, password)
                login.postValue(handleLogin(responce))
            }else{
                login.postValue(Result.Error("-No internet connection"))
            }
        }catch (t: Throwable) {
            when (t) {
                is IOException -> login.postValue(Result.Error("-Network Failure"))
                else -> login.postValue(Result.Error("-Conversion Error"))


            }
        }
    }

    fun registerUser(username :String,
                     password :String,
                     email :String,
                     first_name :String,
                     last_name :String,
                     national_code :String)= viewModelScope.launch{
        safeRegisterUser(username, password, email, first_name, last_name, national_code)
    }
    fun activeUser(
        opt_code:String,
        username :String,
                     password :String,
                     email :String,
                     first_name :String,
                     last_name :String,
                     national_code :String)= viewModelScope.launch{
        safeActiveUser(opt_code,username, password, email, first_name, last_name, national_code)
    }
    fun handleLogin(responce: Response<LoginResponse>):Result<LoginResponse>{

        if (responce.isSuccessful){
            responce.body()?.let {
                return Result.Success(it)
            }
        }
        return Result.Error(responce.message(),responce.body())
    }

    private suspend fun safeActiveUser(optCode: String, username: String, password: String, email: String, firstName: String, lastName: String, nationalCode: String) {

        active.postValue(Result.Loading())

        try {

            if (hasInternetConnection()) {
                val responce = restinoRepository.activeUser(
                    optCode,
                    username,
                    password,
                    email,
                    firstName,
                    lastName,
                    nationalCode
                )
                active.postValue(handleRegister(responce))
            }else{
                active.postValue(Result.Error("-No internet connection"))

            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> active.postValue(Result.Error("-Network Failure"))
                else -> active.postValue(Result.Error("-Conversion Error"))


            }
        }





    }

    private suspend fun safeRegisterUser(
        username: String,
        password: String,
        email: String,
        firstName: String,
        lastName: String,
        nationalCode: String
    ) {
        register.postValue(Result.Loading())

        try {


        if (hasInternetConnection()){
            val responce=restinoRepository.registerUser(username, password, email, firstName, lastName, nationalCode)
            register.postValue(handleRegister(responce))
        }else{
            register.postValue(Result.Error("-No internet connection"))
        }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> register.postValue(Result.Error("-Network Failure"))
                else -> register.postValue(Result.Error("-Conversion Error"))


            }
        }
    }
    fun handleRegister(responce: Response<RegisterResponce>):Result<RegisterResponce>{

        if (responce.isSuccessful){
            responce.body()?.let {
                return Result.Success(it)
            }
        }
        return Result.Error(responce.message(),responce.body())
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<MyApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }


        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }

        return false
    }

}
