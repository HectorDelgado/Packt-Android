package com.soundbite.packt

import android.util.Log
import com.soundbite.packt.domain.RetrofitServiceBuilder
import com.soundbite.packt.network.DogApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import timber.log.Timber

class RetrofitServiceTest {
    private val dogApiURL = "https://api.thedogapi.com/v1/"
    private val badUrl = "https://api.thebadapiurl.comp/v2/"
    private val TAG = "T-${javaClass.simpleName}"
    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

//    @Before
//    fun setup() {
//        Dispatchers.setMain(testDispatcher)
//    }
//
//    @After
//    fun tearDown() {
//        Dispatchers.resetMain()
//        testDispatcher.cleanupTestCoroutines()
//    }
//
//    @Test
//    fun resultsNotEmpty_pass() = runBlockingTest {
//        val serviceBuilder = RetrofitServiceBuilder.getInstance(dogApiURL)
//        val dogApiService = serviceBuilder.createService(DogApi::class.java)
//
//        val dogs = dogApiService.getAllDogs()
//        Log.d(TAG, "Size of dogs is ${dogs.size}")
//        assert(dogs.isNotEmpty())
//    }

//    @Test
//    fun resultsNotEmpty_fail() = runBlockingTest {
////        println("Entering TESTTT")
////        val serviceBuilder = RetrofitServiceBuilder.getInstance(badUrl)
////        val dogApiService = serviceBuilder.createService(DogApi::class.java)
////
////        val dogs = dogApiService.getAllDogs()
////        Log.d(TAG, "Size of dogs is ${dogs.size}")
////        assert(dogs.isNotEmpty())
//    }
}