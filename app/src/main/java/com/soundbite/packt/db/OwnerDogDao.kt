package com.soundbite.packt.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

/**
 * Dao used to interact with the stored data in [UserDatabase].
 */
@Dao
interface OwnerDogDao {
    @Query("SELECT * FROM DogOwner LIMIT 1")
    suspend fun getDogOwner(): DogOwner

    @Query("SELECT * FROM Dog")
    suspend fun getDogs(): List<Dog>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOwner(dogOwner: DogOwner)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDogs(dogs: List<Dog>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateOwner(dogOwner: DogOwner)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateDogs(dogs: List<Dog>)

    @Delete
    suspend fun deleteDogOwner(dogOwner: DogOwner)

    @Delete
    suspend fun deleteDogs(dogs: List<Dog>)
}
