package com.soundbite.packt.db

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class OwnerDogViewModel(private val ownerDogDao: OwnerDogDao) : ViewModel() {
    private val _currentUser = MutableStateFlow(FirebaseAuth.getInstance().currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser

    init {
        _currentUser.value = FirebaseAuth.getInstance().currentUser
    }

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
