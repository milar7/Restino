package com.example.restino.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restino.data.repository.RestinoRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import com.example.restino.data.Result
import com.example.restino.data.remote.responceAllProduct.Products
import com.example.restino.util.NumberEnToFarsi

private const val TAG = "HomeViewModel"

class HomeViewModel(val restinoRepository: RestinoRepository) : ViewModel() {
    val newProducts: MutableLiveData<Result<Products>> = MutableLiveData()


    // TODO do some thing with not to request every single time
    init {
        getNewProducts()
    }
    fun getNewProducts() = viewModelScope.launch {
        newProducts.postValue(Result.Loading())
        val response = restinoRepository.getNewProducts()
        newProducts.postValue(handleNewProductsResponse(response))

    }
    private fun handleNewProductsResponse(response: Response<Products>): Result<Products> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Result.Success(it)
            }
        }
        return Result.Error(response.message())
    }

}