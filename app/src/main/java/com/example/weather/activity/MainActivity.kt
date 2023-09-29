package com.example.weather.activity


import android.annotation.SuppressLint
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
import com.example.weather.utils.SharedPrefs

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    lateinit var weatherViewModel: WeatherVm



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val sharePref = SharedPrefs.getInstence(this)

        var arrayCity = sharePref.getValueOrNull("city")?.toMutableList()
        var arrayGeoLocation = sharePref.getValueOrNull("geolocation")

        Log.d("location", "onCreate: "+arrayGeoLocation)

        if (arrayCity != null && arrayGeoLocation!= null) {
            Log.d("sharedpref","data")
            val locations = Locations(
                arrayCity,
                mutableListOf(geoLocation(arrayGeoLocation?.get(0), arrayGeoLocation?.get(1)))
            )

            setUpViewModel(locations)


       }else{
            Log.d("sharedpref","null")
            sharePref.setValue("geolocation", listOf("40.730610","-73.935242"))
            sharePref.setValue("city", listOf("Delhi"))
            arrayCity = sharePref.getValueOrNull("city")?.toMutableList()
            arrayGeoLocation = sharePref.getValueOrNull("geolocation")
            val locations = Locations(
                arrayCity,
                mutableListOf(geoLocation(arrayGeoLocation?.get(0), arrayGeoLocation?.get(1)))
            )

            setUpViewModel(locations)
//
        }




        binding.imageButtonLocation.setOnClickListener(){
            val intent = Intent(this,cityManager::class.java)
            startActivity(intent)
        }

    }

    @SuppressLint("NewApi")
    private fun setUpViewModel(locations: Locations) {
        weatherViewModel = ViewModelProvider(this).get(WeatherVm::class.java)
        weatherViewModel.getWeatherByLocations(locations)

        weatherViewModel.LiveData.observe(this, Observer {
            var data = it.AllLocations
            data.sortBy { it.priority }

//            Log.d("weatherVm", "onCreate: view model---" + data.toString())

            val adapter = weatherAdapter(this, data)
//
            binding.viewpager2Weathers.adapter = adapter
            binding.viewpager2Weathers.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.textViewLocationName.text = data[position].city

                }
            })

        })

        weatherViewModel.cityName.observe(this, Observer {
            binding.textViewLocationName.text =
                it[binding.viewpager2Weathers.currentItem].toString()
        })
    }

    private fun setUpLocationName(position: Int) {
        val locationlist = listOf<String>("Delhi", "jaipur", "landon","location")
        binding.textViewLocationName.text = locationlist[position]
    }

}