package com.example.restino.data.remote.responceLocations

data class ResponceLocationsItem(
    val address: String,
    val city: Int,
    val id: Int,
    val lat: String,
    val long: String,
    val plaque: String,
    val user: String,
    val zip_code: String
)