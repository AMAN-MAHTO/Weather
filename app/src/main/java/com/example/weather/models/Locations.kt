package com.example.weather.models

import com.google.gson.annotations.SerializedName

data class Locations(
    @SerializedName("cityList"    ) var cityList    : ArrayList<String> = arrayListOf(),
    @SerializedName("geoLocationList"    ) var geoLocationList    : ArrayList<geoLocation> = arrayListOf(),
)

data class geoLocation(
    @SerializedName("lat"     ) var lat     : String?         = null,
    @SerializedName("log"     ) var log     : String?         = null,
)
