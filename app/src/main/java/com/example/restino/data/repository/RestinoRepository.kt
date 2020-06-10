package com.example.restino.data.repository

import com.example.restino.data.local.RestinoDatabase
import com.example.restino.data.model.Product
import com.example.restino.data.remote.RetrofitInctance

class RestinoRepository(
    val db: RestinoDatabase
) {

    //Remote


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
        opt_code: String,
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

    suspend fun getProfile(
        access: String
    ) = RetrofitInctance.api.getProfile(
        access = access
    )

    suspend fun getRefresh(
        refresh: String
    ) = RetrofitInctance.api.refreshToken(
        refresh = refresh
    )

    suspend fun getLocations(
        access: String
    ) = RetrofitInctance.api.getLocations(
        access = access
    )

    suspend fun ceateAddress(
        access: String,
        city: Int,
        address: String,
        zip_code: String,
        plaque: String,
        lat: String,
        long: String
    ) = RetrofitInctance.api.createLocation(
        access = access, city = city
        , address = address, zip_code = zip_code, plaque = plaque,
        lat = lat, long = long
    )

    suspend fun editProfile(
        access: String,
        avatar: String,
        first_name: String,
        last_name: String,
        email: String,
        birth_date: String,
        national_code: String
    ) = RetrofitInctance.api.editProfile(
        access,
        avatar,
        first_name,
        last_name,
        email,
        birth_date,
        national_code
    )


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Local

    suspend fun upsertProduct(product: Product) = db.getProductDao().upsert(product)
    suspend fun deleteProduct(product: Product) = db.getProductDao().deleteProduct(product)

    fun getBasketProducts()=db.getProductDao().getProducts()


//    suspend fun getProductById(id:Int)=
//        RetrofitInctance.api.getProductById(id)

}