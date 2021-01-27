package com.soundbite.packt.model

data class DogBreed(
    val bred_for: String? = null,
    val breed_group: String? = null,
    val height: Height,
    val id: Int,
    val image: Image? = null,
    val life_span: String,
    val name: String,
    val origin: String? = null,
    val reference_image_id: String,
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
