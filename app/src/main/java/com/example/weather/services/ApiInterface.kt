package com.example.weather.services

import com.example.weather.models.ForeCast
import com.example.weather.utils.utils
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("forecast?")
    fun getFiveDaysWeatherDataByGeoLocation(
        @Query("lat") latitude:String,
        @Query("lon") longitude:String,
        @Query("units") unit:String="metric",
        @Query("appid") appid:String=utils.API_KEY,
    ): Call<ForeCast>

    @GET("forecast?")
    fun getFiveDaysWeatherDataByCity(
        @Query("q") city:String,
        @Query("units") unit:String="metric",
        @Query("appid") appid:String=utils.API_KEY
    ): Call<ForeCast>

}