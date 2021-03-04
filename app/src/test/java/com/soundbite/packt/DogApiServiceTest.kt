package com.soundbite.packt

import com.soundbite.packt.domain.RetrofitServiceBuilder
import com.soundbite.packt.model.DogBreed
import com.soundbite.packt.network.DogApi
import java.lang.IllegalStateException
import kotlinx.coroutines.runBlocking
import org.junit.Test

class DogApiServiceTest {
    private val baseUrl = "https://api.thedogapi.com/v1/"
    private val serviceBuilder = RetrofitServiceBuilder.getInstance(baseUrl)
    private val dogApiService = serviceBuilder.createService(DogApi::class.java)
    private var dogBreeds: List<DogBreed>

    init {
        runBlocking {
            println("Retrieving data from server...")
            dogBreeds = dogApiService.getAllDogs()
        }
    }

    @Test
    fun getAllDogs_listNotEmpty_true(): Unit = runBlocking {
        assert(dogBreeds.isNotEmpty())
    }

    @Test
    fun getAllDogs_idValid_true() = runBlocking {
        dogBreeds.forEach {
            if (it.id <= 0)
                throw IllegalStateException("We found a non-positive id: ${it.id}")
        }
    }

    @Test
    fun getAllDogs_nameNotEmpty_true() = runBlocking {
        dogBreeds.forEach {
            if (it.name.isEmpty())
                throw IllegalStateException("We found an empty name!")
        }
    }
}
