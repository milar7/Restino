package com.example.restino.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restino.data.repository.RestinoRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import com.example.restino.data.Result
import com.example.restinoapp.data.remote.ResponceAllProducts.Products


class HomeViewModel(val restinoRepository: RestinoRepository) : ViewModel() {
    val newProducts: MutableLiveData<Result<Products>> = MutableLiveData()

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