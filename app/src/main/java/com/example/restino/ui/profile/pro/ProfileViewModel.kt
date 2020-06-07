package com.example.restino.ui.profile.pro

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
import com.example.restino.data.remote.responseProfile.ProfileResponse
import com.example.restino.data.repository.RestinoRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class ProfileViewModel(
    app: Application,
    val restinoRepository: RestinoRepository,
    val accesS: String
) : AndroidViewModel(app) {
    val profile: MutableLiveData<Result<ProfileResponse>> = MutableLiveData()

    init {
        getProfile(accesS)
    }
    fun getProfile(access :String
    )= viewModelScope.launch{
        safeGetProfile(access)
    }

    private suspend fun safeGetProfile(access: String) {
        profile.postValue(Result.Loading())

        try {

            if (hasInternetConnection()){
                val responce=restinoRepository.getProfile(access)
                profile.postValue(handleGetProfile(responce))
            }else{
                profile.postValue(Result.Error("-No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> profile.postValue(Result.Error("-Network Failure"))
                else -> profile.postValue(Result.Error("-Conversion Error"))


            }
        }

    }

    fun handleGetProfile(responce: Response<ProfileResponse>):Result<ProfileResponse>{

        if (responce.isSuccessful){
            responce.body()?.let {
                return Result.Success(it)
            }
        }
        return Result.Error("${responce.message()}q",responce.body())
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