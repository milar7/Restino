package com.example.restino.data.repository

import com.example.restino.data.remote.RetrofitInctance

class RestinoRepository(
    //val db:RestinoDatabase
){

    suspend fun getNewProducts()=
        RetrofitInctance.api.getProducts()

//    suspend fun getProductById(id:Int)=
//        RetrofitInctance.api.getProductById(id)

}