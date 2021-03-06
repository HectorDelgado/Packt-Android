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
import timber.log.Timber

/**
 * Main Activity for this application. This Activity is mainly used to setup the Toolbar,
 * NavController, and DrawerLayout
 */
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

        initializeComponents()

        setSupportActionBar(toolbar)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(navigationView, navController)

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

    /**
     * Initializes the basic components needed for this Activity.
     */
    private fun initializeComponents() {
        drawerLayout = binding.drawerLayout
        navigationView = binding.navView
        toolbar = binding.toolBar
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.initialFragment
            ),
            drawerLayout
        )
    }
}
