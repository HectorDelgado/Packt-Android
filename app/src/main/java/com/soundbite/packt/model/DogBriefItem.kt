package com.soundbite.packt.model

import android.graphics.Bitmap

data class DogBriefItem(
    val dogName: String,
    val dogBreed: String,
    val dogImg: Bitmap? = null
)
