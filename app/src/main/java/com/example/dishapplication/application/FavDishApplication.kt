package com.example.dishapplication.application

import android.app.Application
import com.example.dishapplication.model.database.FavDishRepository
import com.example.dishapplication.model.database.FavDishRoomDatabase

//need to add this as name in AndroidManifest
class FavDishApplication: Application() {

    private val database by lazy { FavDishRoomDatabase.getDatabase(this) }

    val repository by lazy { FavDishRepository(database.favDishDao()) }
}