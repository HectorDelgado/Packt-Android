package com.soundbite.packt.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Dao used to interact with the stored data in [UserDatabase].
 */
@Dao
interface OwnerDogDao {
    @Transaction
    @Query("SELECT * FROM DogOwner")
    suspend fun getOwnerAndDogs(): OwnerWithDogs

    @Transaction
    @Query("SELECT * FROM DogOwner")
    fun getAllData(): Flow<OwnerWithDogs>

    @Query("SELECT * FROM DogOwner LIMIT 1")
    suspend fun getOwner(): DogOwner

    @Query("SELECT * FROM Dog WHERE dogUid=:id")
    suspend fun getDogByDogOwnerId(id: Int): Dog

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
