package com.soundbite.packt.db

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class OwnerDogViewModel(private val ownerDogDao: OwnerDogDao) : ViewModel() {
    fun getDogOwner(): Flow<User> = flow {
        emit(ownerDogDao.getDogOwner())
    }
    fun getDogs(): Flow<List<Dog>> = flow {
        emit(ownerDogDao.getDogs())
    }
    fun insertDogOwner(user: User) = viewModelScope.launch {
        ownerDogDao.insertOwner(user)
    }
    fun insertDogs(dogs: List<Dog>) = viewModelScope.launch {
        ownerDogDao.insertDogs(dogs)
    }
    fun insertDogOwnerAndDogs(user: User, dogs: List<Dog>) = viewModelScope.launch {
        ownerDogDao.insertOwner(user)
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
