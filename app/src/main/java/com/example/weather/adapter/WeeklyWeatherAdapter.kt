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
import com.example.weather.models.WeatherList
import java.time.LocalDate

class WeeklyWeatherAdapter(val context: Context,val weatherList:List<WeatherList>):
    RecyclerView.Adapter<WeeklyWeatherAdapter.WeeklyWeatherViewHolder>() {
    inner class WeeklyWeatherViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val dayName = itemView.findViewById<TextView>(R.id.textViewWeeklyName)
        val minMaxTemp = itemView.findViewById<TextView>(R.id.textViewweeklyMinMaxTemp)
        val icon= itemView.findViewById<ImageView>(R.id.imageViewWeeklyIcon)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeeklyWeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_weekly_weather_layout,parent,false)
        return WeeklyWeatherViewHolder(view)
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: WeeklyWeatherViewHolder, position: Int) {

            holder.dayName.text =LocalDate.parse(weatherList[position].dtTxt?.substring(0, 10)).dayOfWeek.toString()
            holder.minMaxTemp.text = weatherList[position].main?.temp.toString()
            val imgName = "img" + weatherList[0].weather[0].icon.toString()+"2"
            val imgId =context.getResources().getIdentifier(imgName, "drawable", context.getPackageName())
            holder.icon.setImageResource(imgId)

    }
}