package com.example.weather.mvvm

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.models.WeatherList
import com.example.weather.services.RetrofitInstance

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WeatherVm: ViewModel() {

    var todayWeatherLiveData = MutableLiveData<List<WeatherList>>()
    var upcommingFourDaysForecastLiveData = MutableLiveData<List<WeatherList>>()

    var clossetToCurrentTimeWeather = MutableLiveData<WeatherList>()
    var cityName = MutableLiveData<String>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getWeather(city:String?, lat:String?, log:String?) = viewModelScope.launch(Dispatchers.IO) {
        var todaysWeatherList = mutableListOf<WeatherList>()
        var forecastWeatherList = mutableListOf<WeatherList>()

        val currentDateTime = LocalDateTime.now()
        val currentDate = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))


        val call = if(city!=null){
            RetrofitInstance.api.getFiveDaysWeatherDataByCity(city)
        }else{
            RetrofitInstance.api.getFiveDaysWeatherDataByGeoLocation(lat!!,log!!)
        }

        val response = call.execute()

        if (response.isSuccessful){
            val weatherList = response.body()?.weatherList
            cityName.postValue(response.body()?.city?.name)

            weatherList?.forEach { weather ->
                if (weather.dtTxt!!.split("\\s".toRegex()).contains(currentDate)) {
                    todaysWeatherList.add(weather)
                }else{
                    if (weather.dtTxt!!.split("\\s".toRegex()).contains("12:00:00")) {
                        forecastWeatherList.add(weather)
                    }
                }
            }

            val closestWeather = closestWeather(todaysWeatherList)
            clossetToCurrentTimeWeather.postValue(closestWeather)

            todayWeatherLiveData.postValue(todaysWeatherList)
            upcommingFourDaysForecastLiveData.postValue(forecastWeatherList)

            Log.d("weatherVm", "getWeather: updating live data")

        } else {
            val errorMessage = response.message()
            Log.e("CurrentWeatherError", "Error: $errorMessage")
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getForecast(city: String?, lat: String?, log: String?) = viewModelScope.launch(Dispatchers.IO) {
        var forecastWeatherList = mutableListOf<WeatherList>()

        val currentDateTime = LocalDateTime.now()
        var currentDate = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        val call = if (city != null) {
            RetrofitInstance.api.getFiveDaysWeatherDataByCity(city)
        } else {
            RetrofitInstance.api.getFiveDaysWeatherDataByGeoLocation(lat!!, log!!)
        }

        val response = call.execute()

        if (response.isSuccessful) {
            val weatherList = response.body()?.weatherList

            weatherList?.forEach { weather ->
                if (!weather.dtTxt!!.split("\\s".toRegex()).contains(currentDate)) {
                    forecastWeatherList.add(weather)
                }
            }

            upcommingFourDaysForecastLiveData.postValue(forecastWeatherList)

            Log.d("weatherVm", "getForecast: updating live data ")

        } else {
            val errorMessage = response.message()
            Log.e("CurrentWeatherError", "Error: $errorMessage")


        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun closestWeather(weatherList: List<WeatherList>): WeatherList? {
        val currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
        var closestWeather:WeatherList? = null
        var minTimedif = Int.MAX_VALUE

        for(weather in weatherList){
            val weatherTime = weather.dtTxt?.substring(11,16)

            val timeDiff = Math.abs(
                timeToMinutes(weatherTime.toString()) - timeToMinutes(
                    currentTime
                )
            )
            if (timeDiff < minTimedif){
                minTimedif = timeDiff
                closestWeather = weather
            }

        }
        return closestWeather
    }

    private fun timeToMinutes(time:String): Int {
        val parts = time.split(":")
        return parts[0].toInt() *60 + parts[1].toInt()
    }


}