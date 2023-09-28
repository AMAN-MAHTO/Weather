package com.example.weather.utils

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.weather.models.WeatherList
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class utils {
    // DAILY FORCAST -->> https://api.openweathermap.org/data/2.5/


    companion object {
        val BASE_URL:String = "https://api.openweathermap.org/data/2.5/"
        val API_KEY:String = "360206d45fc68c457ae90b923fa403d0"

        @RequiresApi(Build.VERSION_CODES.O)
//        fun closestWeather(weatherList: List<WeatherList>): WeatherList? {
//            val currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
//            var closestWeather:WeatherList? = null
//            var minTimedif = Int.MAX_VALUE
//
//            for(weather in weatherList){
//                val weatherTime = weather.dtTxt?.substring(11,16)
//
//                val timeDiff = Math.abs(timeToMinutes(weatherTime.toString()) - timeToMinutes(currentTime))
//                if (timeDiff < minTimedif){
//                    minTimedif = timeDiff
//                    closestWeather = weather
//                }
//
//            }
//            return closestWeather
//        }

        fun minMaxTemp(weatherList: List<WeatherList>, date: LocalDateTime): MutableList<Int> {
            var minMaxTemp = mutableListOf<Int>(100,-100)
            var weatherList = weatherListByDate(weatherList,date)


            for (weather in weatherList){
                val temp = weather.main?.temp?.toInt()
                if (temp != null) {
                    if (minMaxTemp[0] > temp) {
                        minMaxTemp[0] = temp
                    }
                    if (minMaxTemp[1] < temp){
                        minMaxTemp[1] = temp
                    }
                }

            }
            Log.d("dateWeather", "minMaxTemp: "+minMaxTemp.toString())
            return minMaxTemp
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun weatherListByDate(weatherList: List<WeatherList>, date: LocalDateTime): MutableList<WeatherList> {
            var date = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            var output = mutableListOf<WeatherList>()
            for (weather in weatherList) {
                Log.d("dateWeather", "weatherListByDate: "+weather.dtTxt?.substring(0,10)+"---"+date)
                if(weather.dtTxt?.substring(0,10) == date){
                    output.add(weather)
                }
            }
            return output
        }

//        private fun timeToMinutes(time:String): Int {
//            val parts = time.split(":")
//            return parts[0].toInt() *60 + parts[1].toInt()
//        }
    }


}