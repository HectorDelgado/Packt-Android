package com.soundbite.packt.db

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

//@Entity
//data class User(
//    val created: Long,
//    val username: String,
//    val firstName: String,
//    val lastName: String,
//    val birthDay: Int,
//    val birthMonth: Int,
//    val birthYear: Int,
//    @PrimaryKey(autoGenerate = true)
//    val userId: Long = 0,
//    val img: String? = null
//)
//
//@Entity
//data class DogBreed(
//    val bredFor: String? = null,
//    val breedGroup: String? = null,
//    @Embedded val height: Height,
//    @Embedded val image: Image? = null,
//    @ForeignKey(
//            entity = User::class,
//            parentColumns = ["userId"],
//            childColumns = ["userCreatorId"],
//            onDelete = CASCADE
//    )
//    val userCreatorId: Long,
//    val lifeSpan: String,
//    val name: String,
//    val origin: String? = null,
//    val referenceImageId: String,
//    val temperament: String? = null,
//    @Embedded val weight: Weight,
//    @PrimaryKey(autoGenerate = true)
//    val dogBreedId: Long = 0,
//)
//
//data class Height(
//    val imperial: String,
//    val metric: String
//)
//
//data class Image(
//    val height: Int,
//    val id: String,
//    val url: String,
//    val width: Int
//)
//
//data class Weight(
//    val imperial: String,
//    val metric: String
//)
//
//@Entity
//data class UserWithDogBreeds(
//    @Embedded val user: User,
//    @Relation(
//            parentColumn = "userId",
//            entityColumn = "userCreatorId"
//    )
//    val dogBreeds: List<DogBreed>
//)
