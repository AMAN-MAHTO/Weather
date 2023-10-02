package com.example.weather.adapter

import android.content.Context
import android.os.Build

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi

import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.models.ForeCast
import kotlin.math.roundToInt


class HourlyWeatherAdapter(val context:Context,val foreCast: ForeCast): RecyclerView.Adapter<HourlyWeatherAdapter.HourlyWeatherViewHolder>() {
    inner class HourlyWeatherViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val time = itemView.findViewById<TextView>(R.id.textViewTimeOfTimeWeatherLyout)
        val temp = itemView.findViewById<TextView>(R.id.textViewTemperatureOfTimeWeatherLyout)
        val img = itemView.findViewById<ImageView>(R.id.imageViewtTimeWeatherLyout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyWeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_time_weather_layout,parent,false)
        return HourlyWeatherViewHolder(view)
    }

    override fun getItemCount(): Int {
            return foreCast.weatherList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: HourlyWeatherViewHolder, position: Int) {

         holder.temp.text = ""+(foreCast.weatherList[position].main?.temp?.roundToInt()).toString()+"°"

        val dateTime = foreCast.weatherList[position].dtTxt.toString()
        holder.time.text = dateTime.subSequence(11,dateTime.length-3)

        val imgName= "img"+foreCast.weatherList[position].weather[0].icon.toString()
        val imgId = context.getResources().getIdentifier(imgName, "drawable", context.getPackageName())
        holder.img.setImageResource(imgId)

    }
}