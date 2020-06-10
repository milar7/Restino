package com.example.restino.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "product_basket")
data class Product(
    val brand: String,
    val categories:String,
    val description: String,
    @PrimaryKey
    val id: Int,
    val image: String,
    val inventory: Int,
    val max_limit: Int,
    val min_limit: Int,
    val name: String,
    val price: Int,
    val properties: String,
    var count:String
)