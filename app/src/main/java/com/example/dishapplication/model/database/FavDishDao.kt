package com.example.dishapplication.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.dishapplication.model.entities.FavDish
import kotlinx.coroutines.flow.Flow

@Dao
interface FavDishDao {

    @Insert
    suspend fun insertDishDetails(favDish: FavDish)

    @Query("SELECT * FROM `FAV _DISHES_TABLE` ORDER BY ID")
    fun getAllDishesList(): Flow<List<FavDish>>

    @Update
    suspend fun updateFavDishDetails(favDish: FavDish)

    @Query("SELECT * FROM `FAV _DISHES_TABLE` WHERE favourite_dish = 1")
    fun getFavouriteDishes(): Flow<List<FavDish>>
}