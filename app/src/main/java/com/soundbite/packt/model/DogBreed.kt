package com.soundbite.packt.model

import com.squareup.moshi.Json

data class DogBreed(
    val id: Int,
    val name: String,
    val height: Height,
    val weight: Weight,
    @Json(name = "reference_image_id")
    val referenceImageId: String,
    @Json(name = "life_span")
    val lifeSpan: String,
    val image: Image? = null,
    val origin: String? = null,
    val temperament: String? = null,
    @Json(name = "bred_for")
    val bredFor: String? = null,
    @Json(name = "breed_group")
    val breedGroup: String? = null
)

data class Height(
    val imperial: String,
    val metric: String
)

data class Image(
    val height: Int,
    val id: String,
    val url: String,
    val width: Int
)

data class Weight(
    val imperial: String,
    val metric: String
)
