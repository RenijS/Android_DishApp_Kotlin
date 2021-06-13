package com.example.dishapplication.model.database

import androidx.room.Dao
import androidx.room.Insert
import com.example.dishapplication.model.entities.FavDish

@Dao
interface FavDishDao {

    @Insert
    suspend fun insertDishDetails(favDish: FavDish)
}