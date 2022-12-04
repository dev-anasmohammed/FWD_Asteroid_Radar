package com.devanasmohammed.asteroidradar

import android.app.Application
import androidx.work.*
import com.devanasmohammed.asteroidradar.data.workers.RefreshAsteroidWork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class App : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }

    private fun delayedInit() {
        applicationScope.launch {
            setupRecurringWork()
        }
    }

    private fun setupRecurringWork() {
        val constraints  = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .build()

        val repeatingWork = PeriodicWorkRequestBuilder<RefreshAsteroidWork>(
            1,
            TimeUnit.DAYS
        ).setConstraints(constraints)
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            "Asteroid_Worker",
            ExistingPeriodicWorkPolicy.REPLACE,
            repeatingWork
        )
    }

}