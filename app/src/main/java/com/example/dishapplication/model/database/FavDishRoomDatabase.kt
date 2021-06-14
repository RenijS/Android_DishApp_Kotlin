package com.example.dishapplication.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dishapplication.model.entities.FavDish

@Database(entities = arrayOf(FavDish::class), version = 1, exportSchema = false)
abstract class FavDishRoomDatabase: RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: FavDishRoomDatabase? = null

        fun getDatabase(context: Context): FavDishRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavDishRoomDatabase::class.java,
                    "word_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}