package com.devanasmohammed.asteroidradar.data

import android.util.Log
import com.devanasmohammed.asteroidradar.data.local.LocalDatabase
import com.devanasmohammed.asteroidradar.data.model.Asteroid
import com.devanasmohammed.asteroidradar.data.model.responses.PictureOfDayResponse
import com.devanasmohammed.asteroidradar.data.remote.RetrofitInstance
import com.devanasmohammed.asteroidradar.util.NetworkUtils
import org.json.JSONObject
import retrofit2.Response

class AsteroidRepository(private val database:LocalDatabase) {
    private val tag = "AsteroidRepository"

    suspend fun getPictureOfTheDay(): Response<PictureOfDayResponse>? {
        try {
            return RetrofitInstance.asteroidApi.getPictureOfTheDay()
        } catch (e: Exception) {
            Log.e(tag, "Catch error in getPictureOfTheDay()!\n${e.message}")
        }
        return null
    }

    suspend fun getRemoteAsteroids(): List<Asteroid>? {
        try {
            val response = RetrofitInstance.asteroidApi1.getAsteroids()
            return NetworkUtils().parseAsteroidsJsonResult(JSONObject(response.toString()))
        } catch (e: Exception) {
            Log.e(tag, "Catch error in getRemoteAsteroids()!\n${e.message}")
        }
        return null
    }

    fun getLocalAsteroids() : List<Asteroid>?{
        try {
            return database.asteroidDao().getAsteroids()
        }catch (e:Exception){
            Log.e(tag,"Catch error in getLocalAsteroids cause: ${e.message}")
        }
        return null
    }

    suspend fun cacheAsteroids(list: List<Asteroid>) {
        try {
             database.asteroidDao().insertAllAsteroids(list)
        }catch (e:Exception){
            Log.e(tag,"Catch error in cacheAsteroids cause: ${e.message}")
        }
    }

}