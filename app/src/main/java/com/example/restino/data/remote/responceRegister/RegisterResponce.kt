package com.example.restino.data.remote.responceRegister

data class RegisterResponce(
    val message: String,
    val errors: Errors?=null,
    val status: Int
)