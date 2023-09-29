package com.example.weather.models

import com.google.gson.annotations.SerializedName

data class Locations(
    @SerializedName("cityList"    ) var cityList: MutableList<String>? = mutableListOf(),
    @SerializedName("geoLocationList"    ) var geoLocationList: MutableList<geoLocation>? = mutableListOf(),
)

data class geoLocation(
    @SerializedName("lat"     ) var lat     : String?         = null,
    @SerializedName("log"     ) var log     : String?         = null,
)
