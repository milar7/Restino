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
import com.example.restino.data.remote.responceCreateAddress.ResponceCreateAddress
import com.example.restino.data.remote.responceLocations.ResponceLocations
import com.example.restino.data.remote.responceRefreshToken.ResponceRefreshToken
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
    val refreshToken: MutableLiveData<Result<ResponceRefreshToken>> = MutableLiveData()
    val createAddress: MutableLiveData<Result<ResponceCreateAddress>> = MutableLiveData()
    val locations:MutableLiveData<Result<ResponceLocations>> = MutableLiveData()


    init {
        getProfile(accesS)
        getLocations(accesS)
    }
    fun getProfile(access :String
    )= viewModelScope.launch{
        safeGetProfile(access)
    }
    fun getLocations(access :String
    )= viewModelScope.launch{
        safeGetLocations(access)
    }

    private suspend fun safeGetLocations(access: String) {
        locations.postValue(Result.Loading())
        try {
            if (hasInternetConnection()){
                val response = restinoRepository.getLocations(access)
                locations.postValue(handleGetLocations(response))
            }else{
                locations.postValue(Result.Error("-No internet connection"))
            }
        }catch (t: Throwable) {
            when (t) {
                is IOException -> locations.postValue(Result.Error("-Network Failure"))
                else -> locations.postValue(Result.Error("-Conversion Error"))
            }
        }    }

    fun getRefresh(refresh :String
    )= viewModelScope.launch{
        safeGetRefresh(refresh)
    }

    fun createAddrees( access:String,
                       city :Int,
                       address :String,
                       zip_code :String,
                       plaque :String,
                       lat :String,
                       long :String)=viewModelScope.launch {
        safeCreateAddress(access, city, address, zip_code, plaque, lat, long)
    }

    private suspend fun safeGetRefresh(refresh: String) {
            refreshToken.postValue(Result.Loading())
        try {
            if (hasInternetConnection()){
                val response = restinoRepository.getRefresh(refresh)
                refreshToken.postValue(handleGetRefresh(response))
            }else{
                refreshToken.postValue(Result.Error("-No internet connection"))
            }
        }catch (t: Throwable) {
            when (t) {
                is IOException -> refreshToken.postValue(Result.Error("-Network Failure"))
                else -> refreshToken.postValue(Result.Error("-Conversion Error"))
            }
        }
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
        return Result.Error("not")
    }
 fun handleGetLocations(responce: Response<ResponceLocations>):Result<ResponceLocations>{

        if (responce.isSuccessful){
            responce.body()?.let {
                return Result.Success(it)
            }
        }
        return Result.Error("not")
    }


    private suspend fun safeCreateAddress( access:String,
                                           city :Int,
                                           address :String,
                                           zip_code :String,
                                           plaque :String,
                                           lat :String,
                                           long :String){
        createAddress.postValue(Result.Loading())
        try {
            if (hasInternetConnection()){
                val response =restinoRepository.ceateAddress(access,city, address, zip_code, plaque, lat, long)
                createAddress.postValue(handleCreateAddress(response))
            }else{
                createAddress.postValue(Result.Error("-No internet connection"))

            }
        }catch (t: Throwable) {
            when (t) {
                is IOException -> createAddress.postValue(Result.Error("-Network Failure"))
                else -> createAddress.postValue(Result.Error("-Conversion Error"))


            }
        }

    }

    fun handleCreateAddress(responce: Response<ResponceCreateAddress>):Result<ResponceCreateAddress>{

        if (responce.isSuccessful){
            responce.body()?.let {
                return Result.Success(it)
            }
        }
        return Result.Error("not")
    }


    fun handleGetRefresh(responce: Response<ResponceRefreshToken>):Result<ResponceRefreshToken>{

        if (responce.isSuccessful){
            responce.body()?.let {
                return Result.Success(it)
            }
        }
        return Result.Error("${responce.message()}not successful refresh",responce.body())
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