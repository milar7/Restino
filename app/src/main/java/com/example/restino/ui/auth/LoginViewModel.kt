package com.example.restino.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.restino.data.repository.RestinoRepository

class LoginViewModel (
    app: Application,
    val restinoRepository: RestinoRepository
) : AndroidViewModel(app) {



}