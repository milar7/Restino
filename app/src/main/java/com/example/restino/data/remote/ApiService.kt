package com.example.restino.data.remote

import com.example.restino.data.remote.responceAllProduct.Products
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("/products/api/v1/products/?format=json")
    suspend fun getProducts(): Response<Products>

//    @GET("products/api/v1/products/:id/")
//    suspend fun getProductById(
//        @Query("id")
//        id:Int
//    ):Response<ItemProduct>
}


