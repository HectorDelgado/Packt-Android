package com.soundbite.packt.util

import android.app.Application
import com.soundbite.packt.BuildConfig
import timber.log.Timber

class ApplicationSetup : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
