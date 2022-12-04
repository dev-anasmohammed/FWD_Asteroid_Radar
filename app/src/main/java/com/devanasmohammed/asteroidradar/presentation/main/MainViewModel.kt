package com.devanasmohammed.asteroidradar.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devanasmohammed.asteroidradar.data.AsteroidRepository
import com.devanasmohammed.asteroidradar.data.model.Asteroid
import com.devanasmohammed.asteroidradar.data.model.responses.PictureOfDayResponse
import com.devanasmohammed.asteroidradar.data.model.responses.Result
import com.devanasmohammed.asteroidradar.util.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Response

class MainViewModel(private val repository: AsteroidRepository) : ViewModel() {

    private val _pictureOfDay = MutableLiveData<Result<PictureOfDayResponse>>()
    val pictureOfDay: LiveData<Result<PictureOfDayResponse>> get() = _pictureOfDay
    private var pictureOfDayResponse: PictureOfDayResponse? = null

    private val _asteroids = MutableLiveData<Result<List<Asteroid>>>()
    val asteroids : LiveData<Result<List<Asteroid>>> get() = _asteroids
    private var asteroidsResponse : List<Asteroid>? = null

    init {
        getPictureOfTheDay()
        getRemoteAsteroids()
    }

    private fun getPictureOfTheDay() {
        viewModelScope.launch {
            _pictureOfDay.postValue(Result.Loading())
            val response = repository.getPictureOfTheDay()
            if(response!=null){
                _pictureOfDay.postValue(handlePictureOfTheDayResponse(response))
            }else{
                _pictureOfDay.postValue(Result.Error("Failed to get picture"))
            }
        }
    }

    private fun handlePictureOfTheDayResponse(response: Response<PictureOfDayResponse>)
            : Result<PictureOfDayResponse> {
        if (response.isSuccessful) {
            try{
                response.body().let {
                    pictureOfDayResponse = it
                }
                return Result.Success(response.body()!!)
            }catch (e:Exception){
                return Result.Error(response.message())
            }
        }
        return Result.Error(response.message())
    }

    private fun getRemoteAsteroids(){
        viewModelScope.launch {
            _asteroids.postValue(Result.Loading())
            //get remote asteroids
            val response = repository.getRemoteAsteroids()
            if(response!=null){
                _asteroids.postValue(Result.Success(response))
                viewModelScope.launch{
                    repository.cacheAsteroids(asteroidsResponse!!)
                }
            }else{
                _asteroids.postValue(Result.Error("Failed to get Asteroids"))
            }
        }
    }

     fun getLocalAsteroids(){
        viewModelScope.launch {
            _asteroids.postValue(Result.Loading())
            //get local asteroids
            var response : List<Asteroid>? = null
            withContext(Dispatchers.IO){
                response = repository.getLocalAsteroids()
            }
            if(response!=null){
                _asteroids.postValue(Result.Success(response!!))
            }else{
                _asteroids.postValue(Result.Error("No local asteroids to display it"))
            }
        }
    }
}