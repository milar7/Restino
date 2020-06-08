package com.example.restino.data.remote

import com.example.restino.data.remote.responceAllProduct.Products
import com.example.restino.data.remote.responceCreateAddress.ResponceCreateAddress
import com.example.restino.data.remote.responceLocations.ResponceLocations
import com.example.restino.data.remote.responceLogin.LoginResponse
import com.example.restino.data.remote.responceRefreshToken.ResponceRefreshToken
import com.example.restino.data.remote.responceRegister.RegisterResponce
import com.example.restino.data.remote.responseEditProfile.ResponsesEditProfile
import com.example.restino.data.remote.responseProfile.ProfileResponse
import retrofit2.Response
import retrofit2.http.*


interface ApiService {

    @GET("/products/api/v1/products/?format=json")
    suspend fun getProducts(): Response<Products>

    @FormUrlEncoded
    @POST("accounts/api/v1/register/")
    suspend fun registerUser(
            @Field("username") username :String,
            @Field("password") password :String,
            @Field("email") email :String,
            @Field("first_name") first_name :String,
            @Field("last_name") last_name :String,
            @Field("national_code") national_code :String
    ): Response<RegisterResponce>


    @FormUrlEncoded
    @POST("accounts/api/v1/activate/")
    suspend fun activeUser(
        @Field("otp_code") otp_code :String,
        @Field("username") username :String,
        @Field("password") password :String,
        @Field("email") email :String,
        @Field("first_name") first_name :String,
        @Field("last_name") last_name :String,
        @Field("national_code") national_code :String
    ): Response<RegisterResponce>



    @FormUrlEncoded
    @POST("accounts/api/v1/authenticate/")
    suspend fun loginUser(
        @Field("username") username :String,
        @Field("password") password :String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("accounts/api/v1/refresh-token/")
    suspend fun refreshToken(
        @Field("refresh") refresh :String
    ): Response<ResponceRefreshToken>

    @GET("accounts/api/v1/profile/")
    suspend fun getProfile(
        @Header("Authorization")access :String
    ): Response<ProfileResponse>

    @GET("accounts/api/v1/my-locations/")
    suspend fun getLocations(
        @Header("Authorization")access :String
    ): Response<ResponceLocations>

    @FormUrlEncoded
    @POST("accounts/api/v1/my-locations/")
    suspend fun createLocation(
        @Header("Authorization")access :String,
        @Field("city")          city :Int,
        @Field("address")   address :String,
        @Field("zip_code") zip_code :String,
        @Field("plaque")    plaque :String,
        @Field("lat")            lat :String,
        @Field("long")          long :String
    ): Response<ResponceCreateAddress>

    @FormUrlEncoded
    @PUT("accounts/api/v1/profile/")
    suspend fun editProfile(
        @Header("Authorization")    access :String,
        @Field("avatar")                    avatar :String,
        @Field("first_name")             first_name :String,
        @Field("last_name")             last_name :String,
        @Field("email")                         email :String,
        @Field("birth_date")                birth_date :String,
        @Field("national_code")          national_code :String
    ): Response<ResponsesEditProfile>


//    @GET("products/api/v1/products/:id/")
//    suspend fun getProductById(
//        @Query("id")
//        id:Int
//    ):Response<ItemProduct>
}


