package com.soundbite.packt.db

import androidx.room.TypeConverter
import com.soundbite.packt.model.DogBreed
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.json.JSONArray

class DogTypeConverter {

    companion object {
        private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        private val jsonAdapter = moshi.adapter(DogBreed::class.java)

        @TypeConverter
        fun fromJSONArrayToList(data: String): List<DogBreed> {
            if (data.isEmpty()) {
                return emptyList()
            }

            val dogBreeds = mutableListOf<DogBreed>()
            val arr = JSONArray(data)

            for (i in 0 until arr.length()) {
                val itemAsString = data[i].toString()
                jsonAdapter.fromJson(itemAsString)?.let {
                    dogBreeds.add(it)
                }
            }

            return dogBreeds
        }

        @TypeConverter
        fun fromListToJSONArray(data: List<DogBreed>): String {
            val jsonArray = JSONArray()

            data.forEach {
                jsonArray.put(jsonAdapter.toJson(it))
            }

            return jsonArray.toString()
        }

    }
}