package com.nasa.apod

import com.nasa.apod.data.API
import com.nasa.apod.data.DataRepository
import com.nasa.apod.data.RemoteRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Factory {

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder().addInterceptor {
            val originalRequest = it.request()
            val newUrl = originalRequest.url().newBuilder().addQueryParameter("api_key", "DEMO_KEY").build()
            it.proceed(originalRequest.newBuilder().url(newUrl).build())
        }.build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder().apply {
            baseUrl("https://api.nasa.gov").addConverterFactory(GsonConverterFactory.create()).client(httpClient)
        }.build()
    }

    private val api: API by lazy {
        retrofit.create(API::class.java)
    }

    val dataRepository: DataRepository by lazy {
        RemoteRepository(api)
    }
}