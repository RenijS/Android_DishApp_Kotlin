package com.example.dishapplication.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.dishapplication.model.entities.FavDish
import kotlinx.coroutines.flow.Flow

@Dao
interface FavDishDao {

    @Insert
    suspend fun insertDishDetails(favDish: FavDish)

    @Query("SELECT * FROM `FAV _DISHES_TABLE` ORDER BY ID")
    fun getAllDishesList(): Flow<List<FavDish>>
}