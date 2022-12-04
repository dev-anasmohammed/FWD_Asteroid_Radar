package com.devanasmohammed.asteroidradar.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.devanasmohammed.asteroidradar.data.model.Asteroid

@Database(
    entities = [
        Asteroid::class
    ], exportSchema = false, version = 1
)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun asteroidDao(): AsteroidDao

    companion object {
        //Volatile - other threads can immediately see when a thread changes this instance
        @Volatile
        private var INSTANCE: LocalDatabase? = null
        fun getDatabase(context: Context): LocalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}