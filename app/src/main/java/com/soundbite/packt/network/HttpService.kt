package com.soundbite.packt.network

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import ru.gildor.coroutines.okhttp.await

/**
 * Singleton object used to make various HTTP request.
 */
object HttpService {
    private val client = OkHttpClient()

    /**
     * Makes a GET request to TheDogApi, await() for the result,
     * then return the Response retrieved if the call was successful
     * or null if any error occurred.
     * Note: The ResponseBody contained within the Response should be
     * closed after it has been used to reduce memory leaks.
     *
     * @return A nullable Response that may contain the result of the GET request.
     */
    suspend fun makeGetRequest(
            url: String,
            headerName: String? = null,
            headerValue: String? = null): Response? {
        val reqBuilder = Request.Builder()
                .url(url)
                .get()

        headerName?.let { name ->
            headerValue?.let { value ->
                reqBuilder.addHeader(name, value)
            }
        }

        return try {
            val response = client.newCall(reqBuilder.build()).await()
            if (response.isSuccessful) {
                response
            } else {
                null
            }
        } catch (e: IOException) {
            Log.d("logz", ">> IOException: ${e.localizedMessage}")
            null
        } catch (e: Exception) {
            Log.d("logz", ">> Exception: ${e.localizedMessage}")
            null
        }
    }
}