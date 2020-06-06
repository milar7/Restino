package com.example.restino.util

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.restino.data.remote.responceAllProduct.ProductsItem
import com.example.restino.data.repository.RestinoRepository
import com.example.restino.ui.auth.AuthViewModel
import com.example.restino.ui.home.HomeViewModel
import com.example.restino.ui.home.detail.DetailViewModel

object InjectorUtil {

    class HomeViewModelProviderFactory
        (val app:Application
        ,val restinoRepository: RestinoRepository)
        :ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return HomeViewModel(app,restinoRepository)as T
        }

    }


    class AuthViewModelProviderFactory
        (val app:Application
         ,val restinoRepository: RestinoRepository)
        :ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return AuthViewModel(app,restinoRepository)as T
        }

    }


    class DetailViewModelProviderFactory
        (val productsItem: ProductsItem)
        :ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return DetailViewModel(productsItem) as T
        }

    }

}