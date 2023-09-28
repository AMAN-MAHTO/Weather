package com.example.weather.activity


import android.app.SearchManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.R
import com.example.weather.adapter.HourlyWeatherAdapter
import com.example.weather.adapter.WeeklyWeatherAdapter
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.services.ApiInterface
import com.example.weather.utils.utils
import com.example.weather.models.ForeCast
import com.example.weather.models.WeatherList
import com.example.weather.mvvm.WeatherVm
import com.example.weather.services.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    lateinit var weatherViewModel:WeatherVm
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
//        fetchWeatherData("delhi")
        weatherViewModel = ViewModelProvider(this).get(WeatherVm::class.java)

        weatherViewModel.getWeather("Jharkhand",null,null)

        weatherViewModel.cityName.observe(this, Observer {
            binding.textViewLocationName.text = it
        })

        weatherViewModel.todayWeatherLiveData.observe(this, Observer{
            setUpView(it[0])

        })

        weatherViewModel.upcommingFourDaysForecastLiveData.observe(this, Observer {
            setUpWeeklyWeather(it)
        })

    }




    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpView(weatherList: WeatherList) {
        Log.d("responseData",""+weatherList.toString())

//        val minMaxTemp = utils.minMaxTemp(List<weatherList>(), LocalDateTime.now())

        if (weatherList != null) {
            binding.textViewWeatherDescription.text = weatherList.weather[0].description
            binding.textViewTemp.text = ""+(weatherList.main?.temp?.roundToInt()).toString()+"°"
//            binding.textViewLocationName.text = responseBody.city?.name.toString()
//            binding.textViewMinMaxTemp.text = "${minMaxTemp[0]}° / ${minMaxTemp[1]}°"
            binding.textViewPressure.text = weatherList.main?.pressure.toString()
            binding.textViewHumidity.text = weatherList.main?.humidity.toString()+"%"
            binding.textViewFeelsLike.text = weatherList.main?.feelsLike.toString()+"°"

            val imgName= "img"+weatherList.weather[0].icon.toString()
            val imgId = this.getResources().getIdentifier(imgName, "drawable", this.getPackageName())
            binding.imageViewIcon.setImageResource(imgId)
        }

        // set lottie animation icon
//        val imgName= "anim"+responseBody.weatherList[0].weather[0].icon.toString()
//        val animId = this.getResources().getIdentifier(imgName, "raw", this.getPackageName())
//        binding.imageViewIcon.setAnimation(animId)
//        binding.imageViewIcon.playAnimation()

//        setUpHourlyWeather(responseBody)

    }

    private fun setUpWeeklyWeather(weatherList:List<WeatherList>) {
        val weeklyWeatherAdapter = WeeklyWeatherAdapter(this, weatherList)

        binding.recyclerViewWeaklyWeather.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        binding.recyclerViewWeaklyWeather.adapter = weeklyWeatherAdapter

    }

    private fun setUpHourlyWeather(responseBody:ForeCast) {
        val houlryWeatherAdapter = HourlyWeatherAdapter(this,responseBody)

        binding.recyclerViewHourlyWeather.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerViewHourlyWeather.adapter = houlryWeatherAdapter

        // using linear lyaout

    }



    @Override
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.top_app_bar, menu)
        Log.d("search", "onCreateOptionsMenu: ")

        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        val appBarSearch = menu?.findItem(R.id.app_bar_search)
        val searchView = appBarSearch?.actionView as SearchView

        searchView.setSearchableInfo(manager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                Log.d("search", "" + query)
                if (query != null) {
//                    fetchWeatherData(query)
                    weatherViewModel.getWeather(query,null,null)
                }

                searchView.onActionViewCollapsed()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })

        return super.onCreateOptionsMenu(menu)
    }
}