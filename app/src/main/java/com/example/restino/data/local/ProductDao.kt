package com.example.restino.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.restino.data.model.Product

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(product: Product)

    @Query("SELECT * FROM product_basket")
     fun getProducts():LiveData<List<Product>>

    @Delete
    suspend fun deleteProduct(product: Product)

}