package com.example.weather.activity


import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.widget.SearchView

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.R
import com.example.weather.adapter.HourlyWeatherAdapter
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.models.ApiInterface
import com.example.weather.utils.utils
import com.example.weather.models.ForeCast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fetchWeatherData("delhi")

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
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                Log.d("search", "" + query)
                if (query != null) {
                    fetchWeatherData(query)
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


    private fun fetchWeatherData(cityName:String){

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(utils.BASE_URL)
            .build().create(ApiInterface::class.java)


        val response = retrofit.getFiveDaysWeatherDataByCity(cityName)
        response.enqueue(object : Callback<ForeCast>{
            override fun onResponse(call: Call<ForeCast>, response: Response<ForeCast>) {
                val responseBody = response.body()
                if (responseBody != null){
                    setUpView(responseBody)

                }

            }

            override fun onFailure(call: Call<ForeCast>, t: Throwable) {
                Log.d("responsedata","fail to get data from api"+t)


            }

        })


    }

    private fun setUpView(responseBody: ForeCast) {
        Log.d("responseData",""+responseBody.toString())

        binding.textViewWeatherDescription.text = responseBody.weatherList[0].weather[0].description
        binding.textViewTemp.text = ""+(responseBody.weatherList[0].main?.temp?.roundToInt()).toString()+"°"
        binding.textViewLocationName.text = responseBody.city?.name.toString()
        binding.textViewMinMaxTemp.text = "${responseBody.weatherList[0].main?.tempMin}° / ${responseBody.weatherList[0].main?.tempMax}°"
        binding.textViewPressure.text = responseBody.weatherList[0].main?.pressure.toString()
        binding.textViewHumidity.text = responseBody.weatherList[0].main?.humidity.toString()+"%"
        binding.textViewFeelsLike.text = responseBody.weatherList[0].main?.feelsLike.toString()+"°"

        val imgName= "img"+responseBody.weatherList[0].weather[0].icon.toString()
        val imgId = this.getResources().getIdentifier(imgName, "drawable", this.getPackageName())
        binding.imageViewIcon.setImageResource(imgId)

        setUpHourlyWeather(responseBody)



    }

    private fun setUpHourlyWeather(responseBody: ForeCast) {
        val houlryWeatherAdapter = HourlyWeatherAdapter(this,responseBody)

        binding.recyclerViewHourlyWeather.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerViewHourlyWeather.adapter = houlryWeatherAdapter

    }
}