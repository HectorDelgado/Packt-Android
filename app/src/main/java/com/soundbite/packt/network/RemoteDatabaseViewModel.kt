package com.soundbite.packt.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.soundbite.packt.model.SingleValueEventResponse
import com.soundbite.packt.util.singleValueEvent
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 *
 */
class RemoteDatabaseViewModel() : ViewModel() {
    /**
     * A Reference to the Firebase Realtime database.
     */
    private val remoteDataBase = Firebase.database.reference

    /**
     * Adds data to the server at /path/uniqueIdentifier if it does not exist.
     *
     * @param path The relative path from this reference to the new one that should be created.
     * @param uniqueIdentifier The uid for the data that will be written at the given path.
     * @param data The data that will be written to the server.
     * @param isSuccess A Lambda function that is passed the success of the operation.
     */
    fun <T> addNewDataToServer(
        path: String,
        uniqueIdentifier: String,
        data: T,
        isSuccess: (Boolean) -> Unit
    ) = viewModelScope.launch {
        if (userSignedIn() && !dataExistsOnServer(path, uniqueIdentifier)) {
            writeToDatabase(path, uniqueIdentifier, data, isSuccess)
        } else {
            isSuccess(false)
        }
    }

    fun <T> updateDataInServer(
        path: String,
        uniqueIdentifier: String,
        property: String,
        data: T,
        isSuccess: (Boolean) -> Unit
    ) = viewModelScope.launch {
        if (userSignedIn() && dataExistsOnServer(path, uniqueIdentifier)) {
            // TODO find better way to write data
        } else {
            isSuccess(false)
        }
    }

    /**
     * Checks if a user is currently signed in.
     *
     * @return True if a user is signed in, false otherwise.
     */
    private fun userSignedIn() = FirebaseAuth.getInstance().currentUser != null

    /**
     * Checks to see if data exists at the specified path with the given uid.
     *
     * @return True if data exists, false otherwise.
     */
    private suspend fun dataExistsOnServer(path: String, uniqueIdentifier: String): Boolean {
        val result = remoteDataBase
            .child(path)
            .child(uniqueIdentifier)
            .singleValueEvent()

        return when (result) {
            is SingleValueEventResponse.Cancelled -> {
                val message = result.error.message
                Timber.e("SingleValueEvent cancelled: $message")
                false
            }
            is SingleValueEventResponse.DataChanged -> {
                val snapshot = result.snapshot
                Timber.e("Snapshot exists: ${snapshot.exists()}")
                snapshot.exists()
            }
        }
    }

    /**
     * Writes data to the remote server at the specified path with with the given uid.
     * This operation may overwrite data if it already exist.
     *
     * @param path The relative path from this reference to the new one that should be created.
     * @param uniqueIdentifier The uid for the data that will be written at the given path.
     * @param data The data that will be written to the server.
     * @param isSuccess A Lambda function that is passed the success of the operation.
     */
    private fun <T> writeToDatabase(
        path: String,
        uniqueIdentifier: String,
        data: T,
        isSuccess: (Boolean) -> Unit
    ) {
        remoteDataBase
            .child(path)
            .child(uniqueIdentifier)
            .setValue(data)
            .addOnSuccessListener {
                isSuccess(true)
            }
            .addOnFailureListener {
                isSuccess(false)
            }
    }
}

/**
 * Factory responsible for implementing the RemoteDataBaseViewModel class.
 */
class RemoteDataBaseViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RemoteDatabaseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RemoteDatabaseViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
