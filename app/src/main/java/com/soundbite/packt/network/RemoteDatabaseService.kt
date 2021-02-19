package com.soundbite.packt.network

import android.content.Context
import android.content.Intent
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.soundbite.packt.db.UserDatabase
import com.soundbite.packt.model.SingletonWithArgHolder

class RemoteDatabaseService private constructor(context: Context) {
    companion object : SingletonWithArgHolder<RemoteDatabaseService, Context>(
        {
            RemoteDatabaseService(it)
        }
    )

    private val remoteDataBase by lazy {
        Firebase.database.reference
    }
    private val localDatabase = UserDatabase.getInstance(context.applicationContext)
    private val firebaseAuth = Firebase.auth

    private fun userExist(): Boolean {
        return firebaseAuth.currentUser != null
    }

    fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    fun createAuthorizationUI(providers: ArrayList<AuthUI.IdpConfig>): Intent {
        return AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
    }
}
