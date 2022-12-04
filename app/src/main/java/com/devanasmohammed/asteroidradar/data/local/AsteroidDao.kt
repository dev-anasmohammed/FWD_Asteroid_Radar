package com.devanasmohammed.asteroidradar.data.local

import androidx.room.*
import com.devanasmohammed.asteroidradar.data.model.Asteroid

@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAsteroids(list: List<Asteroid>)

    @Query("SELECT * FROM asteroids")
    fun getAsteroids() : List<Asteroid>

    @Query("DELETE FROM asteroids")
    fun deleteAllAsteroids()
}