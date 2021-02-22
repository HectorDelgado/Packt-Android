package com.soundbite.packt.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import com.google.android.material.navigation.NavigationView
import com.soundbite.packt.DrawerLocker
import com.soundbite.packt.R
import com.soundbite.packt.databinding.ActivityMainBinding
import com.soundbite.packt.domain.RetrofitServiceBuilder
import com.soundbite.packt.network.DogApi
import timber.log.Timber

class MainActivity : AppCompatActivity(), DrawerLocker {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val TAG = "T-${javaClass.simpleName}"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        drawerLayout = binding.drawerLayout
        toolbar = binding.toolBar
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.initialFragment
            ),
            drawerLayout
        )
        navigationView = binding.navView

        setSupportActionBar(toolbar)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(navigationView, navController)

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

        Timber.tag(TAG).d("Testing in main")
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun setDrawerEnabled(enabled: Boolean) {
        val lockMode = when (enabled) {
            true -> {
                toolbar.visibility = View.VISIBLE
                DrawerLayout.LOCK_MODE_UNLOCKED
            }
            false -> {
                toolbar.visibility = View.GONE
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED
            }
        }

        drawerLayout.setDrawerLockMode(lockMode)
    }
}
