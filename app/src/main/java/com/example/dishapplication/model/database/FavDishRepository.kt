package com.example.dishapplication.model.database

import androidx.annotation.WorkerThread
import com.example.dishapplication.model.entities.FavDish

class FavDishRepository(private val favDishDao: FavDishDao) {

    @WorkerThread
    suspend fun insertFavDIshData(favDish: FavDish){
        favDishDao.insertDishDetails(favDish)
    }
}