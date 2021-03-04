package com.soundbite.packt.util

class WeightConversions {
    companion object {
        fun poundsToKilograms(pounds: Double): Double {
            return pounds * .45359237
        }

        fun kilogramsToPounds(kilograms: Double): Double {
            return kilograms * 2.2046226218
        }
    }
}
