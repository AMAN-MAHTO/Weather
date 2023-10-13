package com.example.weather.adapter

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.toUpperCase
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.models.ForeCast
import com.example.weather.models.Location
import com.example.weather.models.WeatherList
import java.util.Locale


import kotlin.math.roundToInt

class weatherAdapter(val context: Context, var liveData: MutableList<Location>): RecyclerView.Adapter<weatherAdapter.weatherAdapterViewHolder>() {


    inner class weatherAdapterViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val temp = itemView.findViewById<TextView>(R.id.textViewTemp)
        val minMaxTemp = itemView.findViewById<TextView>(R.id.textViewMinMaxTemp)
        val pressure = itemView.findViewById<TextView>(R.id.textViewPressure)
        val humidity = itemView.findViewById<TextView>(R.id.textViewHumidity)
        val feelsLike = itemView.findViewById<TextView>(R.id.textViewFeelsLike)
        val icon = itemView.findViewById<ImageView>(R.id.imageViewIcon)
        val description = itemView.findViewById<TextView>(R.id.textViewWeatherDescription)
        val recyclerViewHourlyWeather = itemView.findViewById<RecyclerView>(R.id.recyclerViewHourlyWeather)
        val recyclerViewWeeklyWeather = itemView.findViewById<RecyclerView>(R.id.recyclerViewWeaklyWeather)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): weatherAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_weather_layout,parent,false)
        return weatherAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return liveData.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: weatherAdapterViewHolder, position: Int) {
        Log.d("weatherVm","onBindViewHolder: "+liveData[position].toString())

        setUpWeeklyWeather(holder,liveData[position].customForeCast.upcomingWeather)
        setUpWeatherView(holder,liveData[position].customForeCast.todaysWeather[1])

    }



    private fun setUpWeatherView(holder: weatherAdapterViewHolder, weatherList: WeatherList?) {

//        holder.locationName.text = foreCast.city?.name.toString()

        if (weatherList != null) {

            holder.temp.text = "" + (weatherList.main?.temp?.roundToInt()).toString() + "°c"
            holder.minMaxTemp.text = "${weatherList.main?.tempMin}°c / ${weatherList.main?.tempMax}°c"
            holder.pressure.text = weatherList.main?.pressure.toString()
            holder.humidity.text = weatherList.main?.humidity.toString() + "%"
            holder.feelsLike.text = weatherList.main?.feelsLike.toString() + "°"
            holder.description.text = weatherList.weather[0].description.toString()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            val imgName = "img" + weatherList.weather[0].icon.toString() + "2"
            val imgId =
                context.getResources().getIdentifier(imgName, "drawable", context.getPackageName())
            holder.icon.setImageResource(imgId)
        }

    }
    private fun setUpWeeklyWeather(holder: weatherAdapterViewHolder,weatherList: List<WeatherList>?) {
        if( weatherList!=null){
        val weeklyWeatherAdapter = WeeklyWeatherAdapter(context, weatherList)

        holder.recyclerViewWeeklyWeather.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        holder.recyclerViewWeeklyWeather.adapter = weeklyWeatherAdapter}

    }

    private fun setUpHourlyWeather(holder: weatherAdapterViewHolder,responseBody:ForeCast) {
        val houlryWeatherAdapter = HourlyWeatherAdapter(context,responseBody)

        holder.recyclerViewHourlyWeather.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        holder.recyclerViewHourlyWeather.adapter = houlryWeatherAdapter

        // using linear lyaout

    }
}