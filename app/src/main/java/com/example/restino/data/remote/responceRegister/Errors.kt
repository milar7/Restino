package com.example.restino.data.remote.responceRegister

data class Errors(
    val username: List<String>?=null,
    val password: List<String>?=null,
    val national_code: List<String>?=null

)