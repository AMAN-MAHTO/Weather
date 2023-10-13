package com.example.weather.activity


import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint

import android.content.Intent
import android.content.pm.PackageManager

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.weather.R

import com.example.weather.adapter.weatherAdapter
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.models.Locations
import com.example.weather.models.geoLocation
import com.example.weather.mvvm.WeatherVm
import com.example.weather.utils.SharedPrefs
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.tasks.CancellationTokenSource


class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    lateinit var weatherViewModel: WeatherVm

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    val  PERMISSION_REQUEST_CODE = 1001


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.lottieAnimationViewLoading.visibility = View.VISIBLE

        // shared pref
        val sharePref = SharedPrefs.getInstence(this)
        var arrayCity = sharePref.getValueOrNull("city")?.toMutableList()
        var arrayGeoLocation = sharePref.getValueOrNull("geolocation")

        // location setup
        ActivityCompat.requestPermissions(
            this, arrayOf(
                ACCESS_COARSE_LOCATION,
                ACCESS_FINE_LOCATION
            ),
            PackageManager.PERMISSION_GRANTED
        )

        val locationRequest: LocationRequest = LocationRequest.Builder(PRIORITY_HIGH_ACCURACY, 100)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(2000)
            .setMaxUpdateDelayMillis(100)
            .build()
        val locationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult == null) {
                    return
                }
            }
        }
        // checking permission
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION),PERMISSION_REQUEST_CODE)
            val result = fusedLocationProviderClient.getCurrentLocation(
//
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                CancellationTokenSource().token
            )
            result.addOnCompleteListener(){
                sharePref.setValue("geolocation", listOf(it.result.latitude.toString(),it.result.longitude.toString()))
                sharePref.setValue("city", listOf("Delhi"))
                // resetting the arrayGeolocation form sharePref
                arrayGeoLocation = sharePref.getValueOrNull("geolocation")
            }
        }

        LocationServices.getFusedLocationProviderClient(getApplicationContext())
            .requestLocationUpdates(locationRequest, locationCallback, null)



        // setting view model according to location
        Log.d("location", "onCreate: "+arrayGeoLocation)

        if (arrayGeoLocation!= null) {
            Log.d("sharedpref","data")
            val locations = Locations(
                arrayCity,
                mutableListOf(geoLocation(arrayGeoLocation?.get(0), arrayGeoLocation?.get(1)))
            )
            setUpViewModel(locations)


       }else{
            Log.d("sharedpref","null")
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            // prevoius saved location
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener {
                if(it != null){
                    sharePref.setValue("geolocation", listOf(it.latitude.toString(),it.longitude.toString()))
                    sharePref.setValue("city", listOf("Delhi")) // in add city, mutable list add, do not add element in null list
                    arrayCity = sharePref.getValueOrNull("city")?.toMutableList()
                    arrayGeoLocation = sharePref.getValueOrNull("geolocation")
                    val locations = Locations(
                        arrayCity,
                        mutableListOf(geoLocation(arrayGeoLocation?.get(0), arrayGeoLocation?.get(1)))
                    )

                    setUpViewModel(locations)

                    Log.d("location", "fetchLocation: not null "+it.toString())
                }else{
                    Log.d("location", "fetchLocation: null ")
                }
            }



            // current location
//            val result = fusedLocationProviderClient.getCurrentLocation(
//
//                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
//                CancellationTokenSource().token
//            )
//            result.addOnCompleteListener(){
//                sharePref.setValue("geolocation", listOf(it.result.latitude.toString(),it.result.longitude.toString()))
//                sharePref.setValue("city", listOf("Delhi"))
//            }

            // get sharedpref and setup view model

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

            val adapter = weatherAdapter(this, data)
            // loding animation invisible
            binding.lottieAnimationViewLoading.visibility = View.INVISIBLE
            // view pager, setup
            binding.viewpager2Weathers.adapter = adapter
            binding.viewpager2Weathers.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.textViewLocationName.text = data[position].city
                    val iconId = data[position].customForeCast.todaysWeather[0].weather[0].icon
                    var backgroundDrawable = R.drawable.bg_sunny
                    when (iconId){
                        "01d" -> backgroundDrawable = R.drawable.bg_sunny
                        "01n" -> backgroundDrawable = R.drawable.bg_night
                        "02n","02d" -> backgroundDrawable = R.drawable.bg_partialy_cloudy
                        "03n","03d","04n","04d"-> backgroundDrawable = R.drawable.bg_cloudy
                        "09n","09d","10n","10d" -> backgroundDrawable = R.drawable.bg_rain
                        "11n","11d" -> backgroundDrawable = R.drawable.bg_thunder_strom
                        "13d" -> backgroundDrawable = R.drawable.bg_snow_day
                        "13n"-> backgroundDrawable = R.drawable.bg_snow_night
                        "50n","50d"-> backgroundDrawable = R.drawable.bg_haze

                    }
                    binding.constraintMain.setBackgroundResource(backgroundDrawable)

                }
            })

            binding.circleIndicator.setViewPager(binding.viewpager2Weathers)

        })

//        weatherViewModel.cityName.observe(this, Observer {
//            binding.textViewLocationName.text =
//                it[binding.viewpager2Weathers.currentItem].toString()
//        })
    }

    private fun setUpLocationName(position: Int) {
        val locationlist = listOf<String>("Delhi", "jaipur", "landon","location")
        binding.textViewLocationName.text = locationlist[position]
    }

}