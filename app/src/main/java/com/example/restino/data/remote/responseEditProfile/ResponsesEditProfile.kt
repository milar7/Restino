package com.example.restino.data.remote.responseEditProfile

data class ResponsesEditProfile(
    val avatar: Any,
    val birth_date: String,
    val date_joined: String,
    val email: String,
    val first_name: String,
    val last_name: String,
    val message: String,
    val national_code: String,
    val status: Int,
    val username: String
)