package com.devanasmohammed.asteroidradar.data.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.devanasmohammed.asteroidradar.data.AsteroidRepository
import com.devanasmohammed.asteroidradar.data.local.LocalDatabase

class RefreshAsteroidWork(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result {
        return try {
            val db = LocalDatabase.getDatabase(applicationContext)
            val repo = AsteroidRepository(db)
            repo.getRemoteAsteroids()?.let {
                repo.cacheAsteroids(it)
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}