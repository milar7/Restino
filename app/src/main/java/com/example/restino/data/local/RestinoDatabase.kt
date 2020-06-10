package com.example.restino.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.restino.data.model.Product

@Database(entities = [Product::class], version = 1)
abstract class RestinoDatabase : RoomDatabase() {

    abstract fun getProductDao():ProductDao
    companion object {
        @Volatile
        private var instance: RestinoDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                RestinoDatabase::class.java,
                "restino_db"
            ).build()


    }
}