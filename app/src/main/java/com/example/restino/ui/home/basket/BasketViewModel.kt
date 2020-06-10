package com.example.restino.ui.home.basket

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.restino.data.model.Product
import com.example.restino.data.repository.RestinoRepository
import kotlinx.coroutines.launch

class BasketViewModel(
    app: Application,
    val restinoRepository: RestinoRepository,
    val accsses: String
):AndroidViewModel(app)
{


    fun addToBasket(product: Product)=viewModelScope.launch {
        restinoRepository.upsertProduct(product)
    }
    fun deleteFromBasket(product: Product)=viewModelScope.launch {
        restinoRepository.deleteProduct(product)
    }

    fun getProduct()=restinoRepository.getBasketProducts()



}