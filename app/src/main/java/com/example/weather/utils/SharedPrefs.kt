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

    fun setValue(key:String,value:List<String>){
        pref.edit().putString(key,value.toString()).apply()
    }

    fun getValue(key:String): List<String>? {
        val arrayString = pref.getString(key, null)
        val arrayList = arrayString?.substring(1,arrayString.length -1)?.split(",")?.map { it.trim() }
        return arrayList
    }

    fun setValueOrNull(key:String?,value:List<String>?){
        if(key != null && value != null){
            pref.edit().putString(key,value.toString()).apply()
        }

    }

    fun getValueOrNull(key:String?): List<String>? {
        if(key != null) {
            val arrayString = pref.getString(key, null)
            val arrayList = arrayString?.substring(1,arrayString.length -1)?.split(",")?.map { it.trim() }
            return arrayList
        }
        return null
    }

    fun clearCityValue(key:String){
        pref.edit().remove(key).apply()
    }

}