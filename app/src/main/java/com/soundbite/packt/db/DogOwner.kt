package com.soundbite.packt.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Defines the data model for a dog owner.
 *
 * @param uid UID for the user.
 * @param created Time of creation for this user, UTC.
 * @param username Username of this account.
 * @param name Name of the user.
 * @param bio Biography for this user.
 * @param birthDay Birth day for this user (must be in range of 1-31)
 * @param birthMonth Birth month for this user (must be in range of 1-12)
 * @param birthYear Birth year for this user (must be greater than 1900)
 * @param img Url location of the icon for this user.
 */
@Entity
data class DogOwner(
    @PrimaryKey
    val uid: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val bio: String,
    val birthDay: Int,
    val birthMonth: Int,
    val birthYear: Int,
    val dogUIDs: List<String>,
    val created: Long,
    val lastLogin: Long,
    val img: String? = null
) {
    constructor() : this("", "", "", "", "", 0, 0, 0, emptyList(), 0, 0)
}
