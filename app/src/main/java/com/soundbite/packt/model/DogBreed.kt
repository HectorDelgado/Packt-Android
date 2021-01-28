package com.soundbite.packt.model

import com.squareup.moshi.Json

data class DogBreed(
    @Json(name = "bred_for")val bredFor: String? = null,
    @Json(name = "breed_group")val breedGroup: String? = null,
    val height: Height,
    val id: Int,
    val image: Image? = null,
    @Json(name = "life_span")val lifeSpan: String,
    val name: String,
    val origin: String? = null,
    @Json(name = "reference_image_id")val referenceImageId: String,
    val temperament: String? = null,
    val weight: Weight
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
