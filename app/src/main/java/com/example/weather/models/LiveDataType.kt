package com.example.weather.models

import com.google.gson.annotations.SerializedName

data class LiveDataType(
    @SerializedName("ListOfAllLocationAndForcast"    ) var  AllLocations   : MutableList<Location> = mutableListOf()
)

data class Location(
    @SerializedName("city"    ) var city    : String,
    @SerializedName("CustomForeCast"    ) var customForeCast: CustomForeCast
)

data class CustomForeCast(
    @SerializedName("todaysWeather"    ) var todaysWeather   : MutableList<WeatherList> = mutableListOf(),
    @SerializedName("upcomingWeather"    ) var upcomingWeather   : MutableList<WeatherList> = mutableListOf()
)