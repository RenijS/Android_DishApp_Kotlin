package com.example.dishapplication.model.network

import com.example.dishapplication.model.entities.RandomDish
import com.example.dishapplication.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RandomDishApiService {

    private val api = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create(RandomDishApi::class.java)

    fun getRandomDish() : Single<RandomDish.Recipes>{
        return api.getDishes(Constants.API_KEY_VALUE
            , Constants.LIMIT_LICENSE_VALUE
            , Constants.TAGS_VALUE
            , Constants.NUMBER_VALUE)
    }
}