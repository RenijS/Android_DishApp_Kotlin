package com.example.dishapplication.model.database

import androidx.annotation.WorkerThread
import com.example.dishapplication.model.entities.FavDish
import kotlinx.coroutines.flow.Flow

class FavDishRepository(private val favDishDao: FavDishDao) {

    @WorkerThread
    suspend fun insertFavDIshData(favDish: FavDish){
        favDishDao.insertDishDetails(favDish)
    }

    val allDishesList: Flow<List<FavDish>> = favDishDao.getAllDishesList()

    @WorkerThread
    suspend fun updateFavDishData(favDish: FavDish){
        favDishDao.updateFavDishDetails(favDish)
    }

    val favouriteDishes: Flow<List<FavDish>> = favDishDao.getFavouriteDishes()

    @WorkerThread
    suspend fun deleteFavDishData(favDish: FavDish){
        favDishDao.deleteFavDishDetails(favDish)
    }

    fun filteredListDishes(value: String) : Flow<List<FavDish>> = favDishDao.getFilteredDishesList(value)
}