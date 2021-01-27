package com.soundbite.packt.network

import com.soundbite.packt.model.DogBreed
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okio.BufferedSource
import org.json.JSONArray
import java.nio.charset.StandardCharsets

/**
 * Class used parse data specifically related to TheDogApi.
 */
class DogBreedService {
    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
    private val jsonAdapter = moshi.adapter(DogBreed::class.java)

    /**
     * Converts the underlying buffer to a String value.
     *
     * @return The string representation of the ByteArray buffer.
     */
    fun bufferedSourceToString(source: BufferedSource): String {
        source.request(Long.MAX_VALUE)
        val bytes = source.buffer.snapshot().toByteArray()
        return String(bytes, StandardCharsets.UTF_8)
    }

    /**
     * Converts the JSONArray to a list of DogBreed objects.
     *
     * @param jsonArray The array to be parsed.
     *
     * @return The list of DogBreed objects represented by the JSONArray.
     */
    fun parseJsonArray(jsonArray: JSONArray): List<DogBreed> {
        val dogBreeds = mutableListOf<DogBreed>()

        for (i in 0 until jsonArray.length()) {
            val item = jsonArray.getJSONObject(i)
            jsonAdapter.fromJson(item.toString())?.let { dogBreed ->
                dogBreeds.add(dogBreed)
            }
        }

        return dogBreeds.toList()
    }
}