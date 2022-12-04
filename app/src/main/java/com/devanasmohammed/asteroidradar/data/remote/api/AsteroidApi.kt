package com.devanasmohammed.asteroidradar.data.remote.api

import com.devanasmohammed.asteroidradar.data.model.Asteroid
import com.devanasmohammed.asteroidradar.data.model.responses.PictureOfDayResponse
import com.devanasmohammed.asteroidradar.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AsteroidApi {
    @GET("planetary/apod")
    suspend fun getPictureOfTheDay(
        @Query("api_key") apiKey: String = API_KEY
    ): Response<PictureOfDayResponse>

    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query("api_key") apiKey: String = API_KEY,
    ): Response<String>
}