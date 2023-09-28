package com.example.weather.activity


import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2

import com.example.weather.adapter.weatherAdapter
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.models.Locations
import com.example.weather.models.geoLocation
import com.example.weather.mvvm.WeatherVm

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    lateinit var weatherViewModel: WeatherVm


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val locations = Locations(
            arrayListOf("Delhi", "jaipur", "landon"),
            arrayListOf(geoLocation("19.076090", "72.877426"))
        )
        val locationlist = listOf<String>("Delhi", "Jaipur", "Landon")

        weatherViewModel = ViewModelProvider(this).get(WeatherVm::class.java)

        weatherViewModel.getWeatherByLocations(locations)


        weatherViewModel.LiveData.observe(this, Observer {
            Log.d("weatherVm", "onCreate: view model---"+it.toString())
            val adapter = weatherAdapter(this, it, locationlist)
//
            binding.viewpager2Weathers.adapter = adapter
            binding.viewpager2Weathers.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    setUpLocationName(position)

                }
            })

        })

        weatherViewModel.cityName.observe(this, Observer {
            binding.textViewLocationName.text =
                it[binding.viewpager2Weathers.currentItem].toString()
        })

        binding.imageButtonLocation.setOnClickListener(){
            val intent = Intent(this,cityManager::class.java)
            startActivity(intent)
        }

    }

    private fun setUpLocationName(position: Int) {
        val locationlist = listOf<String>("Delhi", "jaipur", "landon","location")
        binding.textViewLocationName.text = locationlist[position]
    }

}