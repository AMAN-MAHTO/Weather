package com.example.weather.services

import com.example.weather.utils.utils
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RetrofitInstance {

    companion object{

        private val retrofit by lazy{

            Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(utils.BASE_URL)
                .build()

        }

        val api by lazy {
            retrofit.create(ApiInterface::class.java)
        }

    }

}