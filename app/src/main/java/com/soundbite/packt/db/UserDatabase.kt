package com.soundbite.packt.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.soundbite.packt.model.SingletonWithArgHolder

/**
 * Room database consisting of owners and dogs.
 * Use the [SingletonWithArgHolder.getInstance] method to create
 * a singleton instance of this database.
 */
@Database(entities = [DogOwner::class, Dog::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    companion object
        : SingletonWithArgHolder<UserDatabase, Context>(
            { Room.databaseBuilder(
                    it.applicationContext,
                    UserDatabase::class.java,
                    "user_database")
                    .build()
            }
    )

    abstract fun ownerDogDao(): OwnerDogDao
}
