package com.soundbite.packt.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.soundbite.packt.R
import com.soundbite.packt.databinding.ActivityMainBinding
import com.soundbite.packt.domain.RetrofitServiceBuilder
import com.soundbite.packt.network.DogApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

//    private lateinit var drawerLayout: DrawerLayout
//    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

//        drawerLayout = binding.drawerLayout
//        navigationView = binding.navView
//
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        val navController = navHostFragment.navController
//
//        NavigationUI.setupWithNavController(navigationView, navController)
//        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

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