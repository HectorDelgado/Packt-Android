package com.soundbite.packt.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.soundbite.packt.databinding.ActivityMainBinding
import com.soundbite.packt.domain.RetrofitServiceBuilder
import com.soundbite.packt.network.DogApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // How to use DogApi
        // Get instance of service builder, pass in base url
        val serviceBuilder = RetrofitServiceBuilder.getInstance("https://api.thedogapi.com/v1/")

        // Create service, pass in api interface class reference as param
        val dogApiService = serviceBuilder.createService(DogApi::class.java)

        // Retrieve results in coroutine scope
        // Should run in IO dispatcher
//        CoroutineScope(Dispatchers.IO).launch {
//            // Store results, use as necessary
//            val allDogs = dogApiService.getAllDogs()
//        }
    }
}