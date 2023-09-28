package com.example.weather.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class SharedPrefs internal constructor(private val context: Context){
    companion object{

        private val SHARED_PREF_NAME = "FORECASTDATA"
        private val KEY_CITY = "city"

        @SuppressLint("StaticFieldLeak")
        private var instance:SharedPrefs? = null

        fun getInstence(context: Context):SharedPrefs{
            if(instance == null){
                instance = SharedPrefs(context.applicationContext)
            }
            return instance as SharedPrefs
        }

    }

    private val pref:SharedPreferences by lazy{
        context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE)
    }

    fun setValue(key:String,value:String){
        pref.edit().putString(key,value).apply()
    }

    fun getValue(key:String): String? {
        return pref.getString(key,null)
    }

    fun setValueOrNull(key:String?,value:String?){
        if(key != null && value != null){
            pref.edit().putString(key,value).apply()
        }

    }

    fun getValueOrNull(key:String?): String? {
        if(key != null) {
            return pref.getString(key, null)
        }
        return null
    }

    fun clearCityValue(key:String){
        pref.edit().remove(key).apply()
    }

}