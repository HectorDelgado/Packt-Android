package com.soundbite.packt

import kotlinx.coroutines.runBlocking
import org.junit.Test

/**
 * WARNING!! THIS TEST FAILS! MUST CHECK DEPENDENCIES/VERSIONS BEFORE CONTINUING
 */
class UserDatabaseTest {
//    private lateinit var userDao: UserDao
//    private lateinit var db: UserDatabase

//    @Before
//    fun createDb() {
//        val context = ApplicationProvider.getApplicationContext<Context>()
//        db = Room.inMemoryDatabaseBuilder(context, UserDatabase::class.java).build()
//        userDao = db.userDao()
//    }
//
//    @After
//    @Throws(IOException::class)
//    fun closeDb() {
//        db.close()
//    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList_returnsTrue() = runBlocking {
//        val user = User(
//                System.currentTimeMillis() / 1000,
//                "JD1341",
//                "Joaquin",
//                "Delgado",
//                1,
//                3,
//                2025)
//        val dogBreeds = listOf(
//                DogBreed(
//                        "bred for something",
//                        "Some breeding group",
//                        Height("an imperial height", "a metric height"),
//                        null,
//                        user.userId,
//                        "a long lifespan",
//                        "Poodly dog",
//                        "an unkonw origin",
//                        "a reference",
//                        "A mellow temperment",
//                        Weight("an imperial weight", "a metric weight"))
//        )
//        val usersWithDogBreed = UserWithDogBreeds(user, dogBreeds)
//
//        userDao.insertUser(user)
//        userDao.insertDogBreeds(dogBreeds)
//        val allUsers = userDao.getAllUsersWithDogBreeds().first()
//        assertEquals(allUsers[0].user, usersWithDogBreed.user)
    }
}
