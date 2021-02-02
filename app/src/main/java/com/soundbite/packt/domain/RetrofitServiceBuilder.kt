package com.soundbite.packt.domain

import com.soundbite.packt.model.SingletonWithArgHolder
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Creates an singleton implementation of the API endpoints defined by the serviceClass.
 * This builder has support for Kotlin and uses Moshi for serialization/deserialization.
 *
 * @param base_url The base url of the API endpoint.
 */
class RetrofitServiceBuilder private constructor(base_url: String) {
    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
    private val httpClient = OkHttpClient.Builder()
    private val logging = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BASIC)
    private val retrofitBuilder = Retrofit.Builder()
        .baseUrl(base_url)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
    private var retrofit = retrofitBuilder.build()

    fun <T> createService(serviceClass: Class<T>): T {
        if (!httpClient.interceptors().contains(logging)) {
            httpClient.addInterceptor(logging)
            retrofitBuilder.client(httpClient.build())
            retrofit = retrofitBuilder.build()
        }
        return retrofit.create(serviceClass)
    }

    fun <T> createService(serviceClass: Class<T>, headerName: String? = null, headerValue: String? = null): T {
        if (headerName != null && headerValue != null) {
            httpClient.interceptors().clear()
            httpClient.addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header(headerName, headerValue)
                    .build()
                chain.proceed(request)
            }
            retrofitBuilder.client(httpClient.build())
            retrofit = retrofitBuilder.build()
        }

        return retrofit.create(serviceClass)
    }

    companion object : SingletonWithArgHolder<RetrofitServiceBuilder, String>(::RetrofitServiceBuilder)
}