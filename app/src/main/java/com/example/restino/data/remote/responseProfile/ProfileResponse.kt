package com.example.restino.data.remote.responseProfile

data class ProfileResponse(
    val avatar: String?="",
    val birth_date: String?="",
    val date_joined: String,
    val email: String?="",
    val first_name: String,
    val last_name: String,
    val national_code: String,
    val username: String
)