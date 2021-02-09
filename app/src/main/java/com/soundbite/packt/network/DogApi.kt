package com.soundbite.packt.network

import com.soundbite.packt.model.DogBreed
import com.soundbite.packt.model.DogImage
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DogApi {
    /**
     * GET request for retrieving all DogBreeds that runs in a coroutine scope.
     *
     * @param attach_breed
     * @param page the results page to return, 0 is the first.
     * @param limit the amount of images to return.
     *
     * @return An invocation of a Retrofit method that may contain a list of all DogBreeds.
     */
    @GET("breeds")
    suspend fun getAllDogs(
        @Query("attach_breed") attach_breed: Int? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null
    ): List<DogBreed>

    /**
     * GET request for retrieving the image matching the image_id parameter.
     *
     * @param image_id the id of the image to search for.
     *
     * @return An invocation of a Retrofit method that may contain a DogImage.
     */
    @GET("images/{image_id}")
    suspend fun getImage(@Path("image_id") image_id: String): DogImage
}
