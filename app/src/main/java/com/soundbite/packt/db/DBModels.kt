package com.soundbite.packt.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Defines the data model for a dog owner.
 *
 * @param ownerUid UID for the user.
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
    val ownerUid: String,
    val dogUids: List<String>?,
    val created: Long,
    val lastLogin: Long,
    val username: String,
    val name: String,
    val bio: String,
    val birthDay: Int,
    val birthMonth: Int,
    val birthYear: Int,
    val img: String? = null
)

/**
 * Defines the data model for a dog.
 *
 * @param dogUid UID for this dog.
 * @param dogBreedId ID for this breed of dog [https://docs.thedogapi.com/api-reference/breeds/breeds-list]
 * @param dogOwnerUid Foreign key to relate to parent column.
 * @param created Time of creation for this user, UTC.
 * @param name Name of this dog.
 * @param bio Biography for this user.
 * @param birthDay Birth day for this dog (must be in range 1-31)
 * @param birthMonth Birth month for this dog (must be in range 1-12)
 * @param birthYear Birth year for this dog (must be greater than 1900)
 * @param img Url location of the icon for this user.
 */
@Entity
data class Dog(
    @PrimaryKey
    val dogUid: String,
    val dogOwnerUid: String,
    val dogBreedId: Int,
    val created: Long,
    val name: String,
    val bio: String,
    val birthDay: Int,
    val birthMonth: Int,
    val birthYear: Int,
    val breed: String,
    val img: String? = null
)

class DogOwnerWithDogs(
    // @Embedded val dogOwner: DogOwner,
    // @Relation( parentColumn = "ownerUid", entityColumn = "dogOwnerUid")
    // val dogs: List<Dog>
)
