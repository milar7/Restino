package com.example.restino.data.remote.responceCreateAddress

data class ResponceCreateAddress(
    val address: String,
    val city: Int,
    val id: Int,
    val lat: String,
    val long: String,
    val message: String,
    val plaque: String,
    val user: String,
    val zip_code: String
)