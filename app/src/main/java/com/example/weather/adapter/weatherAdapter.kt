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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.models.ForeCast
import com.example.weather.models.LiveDataType
import com.example.weather.models.WeatherList


import kotlin.math.roundToInt

class weatherAdapter(val context: Context, var liveData: LiveDataType, val locations:List<String>): RecyclerView.Adapter<weatherAdapter.weatherAdapterViewHolder>() {


    inner class weatherAdapterViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val temp = itemView.findViewById<TextView>(R.id.textViewTemp)
        val minMaxTemp = itemView.findViewById<TextView>(R.id.textViewMinMaxTemp)
        val pressure = itemView.findViewById<TextView>(R.id.textViewPressure)
        val humidity = itemView.findViewById<TextView>(R.id.textViewHumidity)
        val feelsLike = itemView.findViewById<TextView>(R.id.textViewFeelsLike)
        val icon = itemView.findViewById<ImageView>(R.id.imageViewIcon)
        val recyclerViewHourlyWeather = itemView.findViewById<RecyclerView>(R.id.recyclerViewHourlyWeather)
        val recyclerViewWeeklyWeather = itemView.findViewById<RecyclerView>(R.id.recyclerViewWeaklyWeather)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): weatherAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_adapter_layout,parent,false)
        return weatherAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return liveData.AllLocations.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: weatherAdapterViewHolder, position: Int) {
        Log.d("weatherVm","onBindViewHolder: "+liveData.AllLocations[position].toString())



        setUpWeeklyWeather(holder,liveData.AllLocations[position].customForeCast.upcomingWeather)
        setUpWeatherView(holder,liveData.AllLocations[position].customForeCast.todaysWeather[0])

    }



    private fun setUpWeatherView(holder: weatherAdapterViewHolder, weatherList: WeatherList?) {

//        holder.locationName.text = foreCast.city?.name.toString()

        if (weatherList != null) {
            holder.temp.text = ""+(weatherList.main?.temp?.roundToInt()).toString()+"°"
            holder.minMaxTemp.text = "${weatherList.main?.tempMin}° / ${weatherList.main?.tempMax}°"
            holder.pressure.text = weatherList.main?.pressure.toString()
            holder.humidity.text = weatherList.main?.humidity.toString()+"%"
            holder.feelsLike.text = weatherList.main?.feelsLike.toString()+"°"

            val imgName= "img"+weatherList.weather[0].icon.toString()
            val imgId = context.getResources().getIdentifier(imgName, "drawable", context.getPackageName())
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