package com.soundbite.packt.util

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.soundbite.packt.model.EventResponse
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun DatabaseReference.singleValueEvent(): EventResponse = suspendCoroutine { continuation ->
    val valueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            continuation.resume(EventResponse.DataChanged(snapshot))
        }

        override fun onCancelled(error: DatabaseError) {
            continuation.resume(EventResponse.Cancelled(error))
        }
    }
    addListenerForSingleValueEvent(valueEventListener)
}