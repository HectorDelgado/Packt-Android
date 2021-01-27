package com.soundbite.packt.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.soundbite.packt.BuildConfig
import com.soundbite.packt.network.DogBreedService
import com.soundbite.packt.R
import com.soundbite.packt.network.HttpService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray

class MainActivity : AppCompatActivity() {
    private val dogBreedService by lazy { DogBreedService() }

    private val httpService by lazy { HttpService }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CoroutineScope(Dispatchers.IO).launch {
            val response = httpService.makeGetRequest(
                    "https://api.thedogapi.com/v1/breeds",
                    "x-api-key",
                    BuildConfig.DOGAPI_KEY)
            response?.body?.let {
                val src = dogBreedService.bufferedSourceToString(it.source())
                val dogBreeds = dogBreedService.parseJsonArray(JSONArray(src))
                it.close()
            }
        }
    }
}