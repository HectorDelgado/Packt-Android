package com.soundbite.packt

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.soundbite.packt.db.Dog
import com.soundbite.packt.db.DogOwner
import com.soundbite.packt.db.OwnerDogDao
import com.soundbite.packt.db.OwnerDogViewModel
import com.soundbite.packt.db.OwnerDogViewModelFactory
import com.soundbite.packt.db.UserDatabase
import java.io.IOException
import java.util.UUID
import kotlin.jvm.Throws
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    private lateinit var userDao: OwnerDogDao
    private lateinit var db: UserDatabase
    private lateinit var dogOwnerViewModel: OwnerDogViewModel

//    @get:Rule
//    val instantExeutorRule = InstantTaskExecutorRule()

    private val dogs = listOf(
        Dog(
            UUID.randomUUID().toString(),
            15461,
            System.currentTimeMillis() / 1000,
            "Sparky",
            "A simple biography.",
            3,
            4,
            1990,
            "Siberian Husky",
            14.5,
            23.5,
            "Male",
            null
        ),
        Dog(
            UUID.randomUUID().toString(),
            538,
            System.currentTimeMillis() / 1000,
            "Fluffy",
            "The cutest dog, but not the smartest.",
            5,
            12,
            2009,
            "Poodle",
            14.5,
            23.5,
            "Male",
            null
        ),
        Dog(
            UUID.randomUUID().toString(),
            18344,
            System.currentTimeMillis() / 1000,
            "Ace",
            "Just a normal dog traveling the world.",
            1,
            16,
            2019,
            "German Shepherd",
            14.5,
            23.5,
            "Female",
            null
        )
    )

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, UserDatabase::class.java).build()
        userDao = db.ownerDogDao()
        dogOwnerViewModel = OwnerDogViewModelFactory(userDao).create(OwnerDogViewModel::class.java)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun userDatabase_addDogOwner_usernameMatches() = runBlocking {
        val dogOwner = DogOwner(
            "JENKINS1073",
            null,
            System.currentTimeMillis() / 1000,
            System.currentTimeMillis() / 1000,
            "Joaquin",
            "Delgado",
            "I have no dogs",
            6,
            3,
            1992,
            null
        )

        dogOwnerViewModel.insertDogOwner(dogOwner)
        val result = dogOwnerViewModel.getDogOwner().single()
        assertEquals("JENKINS1073", result.username)
        db.clearAllTables()
    }

    @Test
    fun userDatabase_addDogs_sizeMatches() = runBlocking {
        dogOwnerViewModel.insertDogs(dogs)
        val result = dogOwnerViewModel.getDogs().single()
        assertEquals(3, result.size)
        db.clearAllTables()
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.soundbite.packt", appContext.packageName)
    }
}
