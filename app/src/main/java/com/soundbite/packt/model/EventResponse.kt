package com.soundbite.packt.model

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

sealed class EventResponse {
    data class DataChanged(val snapshot: DataSnapshot) : EventResponse()
    data class Cancelled(val error: DatabaseError) : EventResponse()
}
