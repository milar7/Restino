package com.example.restino.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.restino.data.repository.RestinoRepository
import com.example.restino.ui.home.HomeViewModel

object InjectorUtil {

    class HomeViewModelProviderFactory
        (val restinoRepository: RestinoRepository)
        :ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return HomeViewModel(restinoRepository)as T
        }

    }

}