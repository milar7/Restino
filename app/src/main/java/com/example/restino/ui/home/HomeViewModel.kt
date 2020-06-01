package com.example.restino.ui.home

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.restino.MyApplication
import com.example.restino.data.Result
import com.example.restino.data.remote.responceAllProduct.Products
import com.example.restino.data.repository.RestinoRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

private const val TAG = "HomeViewModel"

class HomeViewModel(
    app: Application,
    val restinoRepository: RestinoRepository
) : AndroidViewModel(app) {
    val newProducts: MutableLiveData<Result<Products>> = MutableLiveData()


    // TODO do some thing with not to request every single time
    init {
        getNewProducts()
    }

    fun getNewProducts() = viewModelScope.launch {
        safeNewProductsCall()

    }

    private fun handleNewProductsResponse(response: Response<Products>): Result<Products> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Result.Success(it)
            }
        }
        return Result.Error(response.message())
    }

    private suspend fun safeNewProductsCall() {
        newProducts.postValue(Result.Loading())
        try {
            if (hasInternetConnection()) {
                val response = restinoRepository.getNewProducts()
                newProducts.postValue(handleNewProductsResponse(response))
            } else {
                newProducts.postValue(Result.Error("-No internet connection"))
            }


        } catch (t: Throwable) {
            when (t) {
                is IOException -> newProducts.postValue(Result.Error("-Network Failure"))
                else -> newProducts.postValue(Result.Error("-Conversion Error"))


            }
        }
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
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }


        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }

        return false
    }
}