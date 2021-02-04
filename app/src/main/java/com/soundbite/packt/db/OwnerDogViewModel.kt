package com.soundbite.packt.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class OwnerDogViewModel(private val ownerDogDao: OwnerDogDao) : ViewModel() {
    fun getOwnerAndDogs(): Flow<OwnerWithDogs> = flow {
        emit(ownerDogDao.getOwnerAndDogs())
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