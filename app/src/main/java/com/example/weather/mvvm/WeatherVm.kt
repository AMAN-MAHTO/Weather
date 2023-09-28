package com.example.weather.mvvm

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.models.CustomForeCast
import com.example.weather.models.LiveDataType
import com.example.weather.models.Location
import com.example.weather.models.Locations
import com.example.weather.models.WeatherList
import com.example.weather.services.RetrofitInstance

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WeatherVm: ViewModel() {


    var clossetToCurrentTimeWeather = MutableLiveData<List<WeatherList>>()
    var cityName = MutableLiveData<List<String>>()

    var LiveData = MutableLiveData<LiveDataType>()
    var listOfAllLocationCustomForeCast = mutableListOf<Location>()

    var currentWeatherListOfAllLocations = mutableListOf<WeatherList>()
    var cityNamesOfAllLocations = mutableListOf<String>()



    @RequiresApi(Build.VERSION_CODES.O)
    fun getWeatherByLocations(locations: Locations) = viewModelScope.launch(Dispatchers.IO) {

        for (location in locations.geoLocationList){
            getWeather(null,location.lat,location.log)

        }
        for (location in locations.cityList){

            getWeather(location,null,null)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getWeather(city:String?, lat:String?, log:String?): Any = viewModelScope.launch(Dispatchers.IO) {
        var todaysWeatherList = mutableListOf<WeatherList>()
        var forecastWeatherList = mutableListOf<WeatherList>()
        var cityname:String

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
            cityname = response.body()?.city?.name.toString()

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
            if (closestWeather != null) {
                currentWeatherListOfAllLocations.add(closestWeather)
            }
            cityNamesOfAllLocations.add(cityname)

            listOfAllLocationCustomForeCast.add(Location(cityname,CustomForeCast(todaysWeatherList,forecastWeatherList)))
            Log.d("weatherVm", "listOfAllLocationCustomForeCast:  "+listOfAllLocationCustomForeCast.size.toString())

            LiveData.postValue(LiveDataType(listOfAllLocationCustomForeCast))
            clossetToCurrentTimeWeather.postValue(currentWeatherListOfAllLocations)
            cityName.postValue(cityNamesOfAllLocations)

            Log.d("weatherVm", "getWeather: live date get updated")

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