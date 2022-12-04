package com.devanasmohammed.asteroidradar.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.devanasmohammed.asteroidradar.data.AsteroidRepository

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(
    private val repository: AsteroidRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }

}