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
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2

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
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY

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
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION),PERMISSION_REQUEST_CODE)
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
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener {
                if(it != null){
                    sharePref.setValue("geolocation", listOf(it.latitude.toString(),it.longitude.toString()))

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