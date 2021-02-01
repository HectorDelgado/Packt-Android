package com.soundbite.packt.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    // GET ONE
    @Transaction
    @Query("SELECT * FROM user WHERE userId=:userId")
    fun getAllUsersWithDogBreedsById(userId: Long): UserWithDogBreeds

    // GET ALL
    @Transaction
    @Query("SELECT * FROM user")
    fun getAllUsersWithDogBreeds(): Flow<List<UserWithDogBreeds>>

    // Update
    @Transaction
    @Update
    fun updateUser(user: UserWithDogBreeds)

    // GET ALL BY FILTER


    // INSERT ONE
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDogBreeds(dogBreeds: List<DogBreed>)

    // INSERT ALL
    @Transaction
    @Insert
    suspend fun insertAllUsers(vararg users: UserWithDogBreeds)

    // DELETE ONE
    @Transaction
    @Delete
    fun deleteUser(user: UserWithDogBreeds)

    // DELETE ALL
}