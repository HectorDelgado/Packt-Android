package com.soundbite.packt.domain

import com.soundbite.packt.network.DogApi

class MainRepository(private val apiService: DogApi) {
    suspend fun getAllDogs() = apiService.getAllDogs()
}