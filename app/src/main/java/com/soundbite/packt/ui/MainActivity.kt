package com.soundbite.packt.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.soundbite.packt.BuildConfig
import com.soundbite.packt.network.DogBreedService
import com.soundbite.packt.R
import com.soundbite.packt.databinding.ActivityMainBinding
import com.soundbite.packt.network.HttpService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val dogBreedService by lazy { DogBreedService() }

    private val httpService by lazy { HttpService }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        val navController = navHostFragment.navController

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