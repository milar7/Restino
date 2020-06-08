package com.example.restino.data.model

data class User (
    var email: String,
    var first_name: String,
    var last_name: String,
    var national_code: String,
    var password: String,
    var username: String,
    var accessToken:String,
    var refreshToken:String,
    var birth_day:String
)