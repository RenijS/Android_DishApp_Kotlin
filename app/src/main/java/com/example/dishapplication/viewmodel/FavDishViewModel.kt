package com.example.dishapplication.viewmodel

import androidx.lifecycle.*
import com.example.dishapplication.model.database.FavDishRepository
import com.example.dishapplication.model.entities.FavDish
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class FavDishViewModel(private val repository: FavDishRepository): ViewModel() {

    fun insert(dish: FavDish) = viewModelScope.launch {
        repository.insertFavDIshData(dish)
    }

    val allDishList: LiveData<List<FavDish>> = repository.allDishesList.asLiveData()

    fun update(dish: FavDish) = viewModelScope.launch {
        repository.updateFavDishData(dish)
    }

    val favouriteDishes: LiveData<List<FavDish>> = repository.favouriteDishes.asLiveData()

    fun delete(dish: FavDish) = viewModelScope.launch {
        repository.deleteFavDishData(dish)
    }

    fun getFilteredList(value: String) : LiveData<List<FavDish>> = repository.filteredListDishes(value).asLiveData()
}

@Suppress("UNCHECKED_CAST")
class FavDishViewModelFactory(private val repository: FavDishRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavDishViewModel::class.java)){
            return FavDishViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}