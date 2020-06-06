package com.example.restino.data.repository

import com.example.restino.data.remote.RetrofitInctance

class RestinoRepository(
    //val db:RestinoDatabase
) {

    suspend fun getNewProducts() =
        RetrofitInctance.api.getProducts()

    suspend fun registerUser(
        username: String,
        password: String,
        email: String,
        first_name: String,
        last_name: String,
        national_code: String
    ) = RetrofitInctance.api.registerUser(
        username,
        password,
        email,
        first_name,
        last_name,
        national_code
    )


    suspend fun activeUser(
        opt_code:String,
        username: String,
        password: String,
        email: String,
        first_name: String,
        last_name: String,
        national_code: String
    ) = RetrofitInctance.api.activeUser(
        opt_code,
        username,
        password,
        email,
        first_name,
        last_name,
        national_code
    )


    suspend fun loginUser(
        username: String,
        password: String
    ) = RetrofitInctance.api.loginUser(
        username,
        password
    )



//    suspend fun getProductById(id:Int)=
//        RetrofitInctance.api.getProductById(id)

}