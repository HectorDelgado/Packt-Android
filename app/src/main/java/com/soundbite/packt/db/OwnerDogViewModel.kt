package com.soundbite.packt.db

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class OwnerDogViewModel(private val ownerDogDao: OwnerDogDao) : ViewModel() {
    fun getDogOwner(): Flow<DogOwner> = flow {
        emit(ownerDogDao.getDogOwner())
    }
    fun getDogs(): Flow<List<Dog>> = flow {
        emit(ownerDogDao.getDogs())
    }
    fun insertDogOwnerAndDogs(dogOwner: DogOwner, dogs: List<Dog>) = viewModelScope.launch {
        ownerDogDao.insertOwner(dogOwner)
        ownerDogDao.insertDogs(dogs)
    }

}

class OwnerDogViewModelFactory(private val repository: OwnerDogDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OwnerDogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OwnerDogViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
