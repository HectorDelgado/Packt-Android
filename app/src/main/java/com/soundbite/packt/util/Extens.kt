package com.soundbite.packt.util

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.soundbite.packt.model.SingleValueEventResponse
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.round

/**
 * Extension function for executing a [ValueEventListener] in a continuation block.
 * The current thread is suspended until the execution is complete at which point
 * the control flow transfers back to the calling thread.
 *
 * @return A SingleValueEventResponse which will hold either a [DatabaseError] or a [DataSnapshot].
 */
suspend fun DatabaseReference.singleValueEvent():
    SingleValueEventResponse = suspendCoroutine { continuation ->
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                continuation.resume(SingleValueEventResponse.Cancelled(error))
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                continuation.resume(SingleValueEventResponse.DataChanged(snapshot))
            }
        }
        addListenerForSingleValueEvent(valueEventListener)
    }

/**
 * Rounds a Double value to a certain number of decimal points.
 *
 * @param decimals The number of decimals to round to.
 *
 * @return A Double value rounded.
 */
fun Double.roundTo(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}
