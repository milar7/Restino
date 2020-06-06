package com.example.restino.data.model

data class User (
    val email: String,
    val first_name: String,
    val last_name: String,
    val national_code: String,
    val password: String,
    val username: String,
    var accessToken:String,
    var refreshToken:String
)