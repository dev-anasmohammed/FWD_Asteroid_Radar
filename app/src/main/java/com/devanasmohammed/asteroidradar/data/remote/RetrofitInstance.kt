package com.devanasmohammed.asteroidradar.data.remote

import com.devanasmohammed.asteroidradar.util.Constants.Companion.BASE_URL
import com.devanasmohammed.asteroidradar.data.remote.api.AsteroidApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class RetrofitInstance {
    companion object{
        //moshi
        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)
                .build()
        }

        //ScalarsConverterFactory
        private val retrofit1 by lazy {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(client)
                .build()
        }

        val asteroidApi: AsteroidApi by lazy{
            retrofit.create(AsteroidApi::class.java)
        }

        val asteroidApi1: AsteroidApi by lazy{
            retrofit1.create(AsteroidApi::class.java)
        }

    }
}