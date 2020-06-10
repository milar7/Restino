package com.example.restino.util

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.restino.data.remote.responceAllProduct.ProductsItem
import com.example.restino.data.repository.RestinoRepository
import com.example.restino.ui.auth.AuthViewModel
import com.example.restino.ui.home.HomeViewModel
import com.example.restino.ui.home.basket.BasketViewModel
import com.example.restino.ui.home.detail.DetailViewModel
import com.example.restino.ui.profile.pro.ProfileViewModel

object InjectorUtil {

    class HomeViewModelProviderFactory
        (
        val app: Application
        , val restinoRepository: RestinoRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return HomeViewModel(app, restinoRepository) as T
        }

    }


    class AuthViewModelProviderFactory
        (
        val app: Application
        , val restinoRepository: RestinoRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return AuthViewModel(app, restinoRepository) as T
        }

    }

    class ProfileViewModelProviderFactory
        (
        val app: Application
        , val restinoRepository: RestinoRepository,
        val accsses: String
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ProfileViewModel(app, restinoRepository, accsses) as T
        }

    }
    class BasketViewModelProviderFactory
        (
        val app: Application
        , val restinoRepository: RestinoRepository,
        val accsses: String
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return BasketViewModel(app, restinoRepository,accsses) as T
        }

    }

    class DetailViewModelProviderFactory
        (
        val productsItem: ProductsItem) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return DetailViewModel(productsItem) as T
        }

    }

}