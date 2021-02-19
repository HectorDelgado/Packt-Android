package com.soundbite.packt

import android.util.Log
import com.soundbite.packt.db.Dog
import com.soundbite.packt.db.DogOwner
import java.util.UUID
import kotlin.random.Random

class MockDataCreator(private val userUid: String, private val created: Long) {
    // Keep this list alphabetized
    private val dogNames = listOf(
        "Alpha",
        "Apollo",
        "Archer",
        "Archie",
        "Bandit",
        "Bolt",
        "Copper",
        "Delta",
        "Dogma",
        "Eskimo",
        "Fluffy",
        "Hero",
        "Hunter",
        "Mocha",
        "Oso",
        "Phoenix",
        "Rex",
        "Rider",
        "Spike",
        "Tango",
        "Wolf",
    )

    private val bios = listOf(
        "Falls down a lot. Explorer. Tv practitioner. Amateur writer. Twitter fanatic. " +
            "Freelance travel geek. Professional pop culture expert.",
        "Zombie buff. Proud analyst. Explorer. Social media fan. Pop culture nerd. " +
            "Bacon specialist.",
        "Social media buff. Internet enthusiast. Creator. Falls down a lot. " +
            "Reader. Twitter guru. Analyst.",
        "Wannabe explorer. Zombie geek. Bacon fan. Creator. Certified entrepreneur. Internet nerd.",
        "Beer trailblazer. Alcohol expert. Amateur introvert. Twitter lover. Gamer.",
        "Communicator. Web specialist. Organizer. Award-winning coffee fanatic. Typical introvert.",
        "Student. Analyst. Web aficionado. Travel trailblazer. Tv maven. Twitter fan. " +
            "Bacon practitioner. Evil music ninja.",
        "Beer fan. Prone to fits of apathy. Thinker. Travel enthusiast. Food guru. " +
            "Alcohol practitioner. Pop culture ninja.",
        "Hipster-friendly bacon buff. Reader. Web fan. Professional explorer. " +
            "Certified internet fanatic. Evil zombie expert.",
        "Music practitioner. Zombie evangelist. Explorer. Travel maven. Communicator. " +
            "Twitteraholic. Pop culture ninja.",
        "Bacon buff. Student. Friendly beer ninja. Hipster-friendly social media advocate. Gamer.",
        "Hipster-friendly introvert. Thinker. Typical music enthusiast. " +
            "Future teen idol. Web expert. Travel buff. Communicator.",
        "Food guru. Freelance explorer. General bacon nerd. Pop culture fan. " +
            "Hipster-friendly tv evangelist.",
        "Pop culture guru. Music fanatic. Food fan. Zombie junkie. " +
            "General travel advocate. Twitter lover.",
        "Zombie nerd. Friend of animals everywhere. Student. Tv advocate. " +
            "Proud thinker. Internet fanatic.",
        "Coffee evangelist. Internet fanatic. Wannabe introvert. Organizer. Creator.",
        "Entrepreneur. Internet expert. Infuriatingly humble music ninja. " +
            "Beer specialist. Organizer.",
        "Webaholic. Problem solver. Explorer. Gamer. Devoted pop culture enthusiast.",
        "Extreme travel aficionado. Bacon lover. Passionate entrepreneur. Gamer. " +
            "Foodaholic. Social media maven. Web fanatic.",
        "Subtly charming coffee expert. Incurable reader. Troublemaker. " +
            "Unable to type with boxing gloves on. Internet advocate.",
        "Social media enthusiast. General bacon lover. Alcohol fanatic. " +
            "Food evangelist. Music buff.",
        "Troublemaker. Web evangelist. Reader. Internet guru. Problem solver. " +
            "Zombieaholic. Bacon enthusiast. Alcohol maven. Beer nerd. Explorer",
        "Wannabe food lover. Amateur thinker. Writer. Tv advocate. " +
            "Total entrepreneur. Coffee fanatic.",
        "Unapologetic beeraholic. Travel ninja. Coffee aficionado. " +
            "Award-winning music fanatic. Twitter buff. Introvert. Entrepreneur."
    )
    private val random = Random
    private var dogUIDs = mutableListOf<String>()

    val dogs by lazy {
        val numberOfDogs = 4
        val dogs = mutableListOf<Dog>()
        for (i in 0..numberOfDogs) {
            val dogUid = generateDogUID()
            dogs.add(
                Dog(
                    dogUid,
                    userUid,
                    generateBreedID(),
                    created,
                    getDogName(),
                    getBio(),
                    generateBirthDay(),
                    generateBirthMonth(),
                    generateBirthYear(),
                    "A Dog Breed",
                    null
                )
            )
            dogUIDs.add(dogUid)
        }
        Log.d("logz", "Created ${dogs.size} dogs")

        dogs.toList()
    }

    val dogOwner by lazy {
        DogOwner(
            userUid,
            dogUIDs,
            created,
            created,
            getDogName(),
            getDogName(),
            getBio(),
            generateBirthDay(),
            generateBirthMonth(),
            generateBirthYear(),
            null
        )
    }

    private fun initializeDogs() {
    }

    private fun generateDogUID() = "${userUid}_${UUID.randomUUID()}"
    private fun generateBreedID() = random.nextInt(50000)
    private fun getDogName() = dogNames.random()
    private fun getBio() = bios.random()
    private fun generateBirthDay() = random.nextInt(1, 32)
    private fun generateBirthMonth() = random.nextInt(1, 13)
    private fun generateBirthYear() = random.nextInt(1900, 2020)
}
