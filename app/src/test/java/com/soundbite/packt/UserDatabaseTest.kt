package com.soundbite.packt

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.soundbite.packt.db.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.IOException
import java.lang.Exception
import java.util.*

/**
 * WARNING!! THIS TEST FAILS! MUST CHECK DEPENDENCIES/VERSIONS BEFORE CONTINUING
 */
class UserDatabaseTest {
    private lateinit var userDao: UserDao
    private lateinit var db: UserDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, UserDatabase::class.java).build()
        userDao = db.userDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList_returnsTrue() = runBlocking {
        val user = User(
                System.currentTimeMillis() / 1000,
                "JD1341",
                "Joaquin",
                "Delgado",
                1,
                3,
                2025)
        val dogBreeds = listOf(
                DogBreed(
                        "bred for something",
                        "Some breeding group",
                        Height("an imperial height", "a metric height"),
                        null,
                        user.userId,
                        "a long lifespan",
                        "Poodly dog",
                        "an unkonw origin",
                        "a reference",
                        "A mellow temperment",
                        Weight("an imperial weight", "a metric weight"))
        )
        val usersWithDogBreed = UserWithDogBreeds(user, dogBreeds)

        userDao.insertUser(user)
        userDao.insertDogBreeds(dogBreeds)
        val allUsers = userDao.getAllUsersWithDogBreeds().first()
        assertEquals(allUsers[0].user, usersWithDogBreed.user)
    }
}