package com.soundbite.packt.model

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

/**
 * State holder for Firebase Realtime Database single value event.
 * The two states represent the onCancelled() and onDataChange() callback events.
 */
sealed class SingleValueEventResponse {
    data class Cancelled(val error: DatabaseError) : SingleValueEventResponse()
    data class DataChanged(val snapshot: DataSnapshot) : SingleValueEventResponse()
}
