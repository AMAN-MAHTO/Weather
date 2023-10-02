package com.example.weather.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.activity.cityManager
import com.example.weather.utils.SharedPrefs

class addCityAdapter(val context: Context, var array:MutableList<String>,var country:MutableList<String>):
    RecyclerView.Adapter<addCityAdapter.addCityViewHolder>() {
    val sharePref = SharedPrefs.getInstence(context)
    var sharedPrefArray = sharePref.getValue("city")?.toMutableList()
    val intent = Intent(context,cityManager::class.java)
    inner class addCityViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val cityName = itemView.findViewById<TextView>(R.id.textViewCityName)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): addCityViewHolder {
        val view =  LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_add_city_layout, parent, false)
        return addCityViewHolder(view)
    }

    override fun getItemCount(): Int {
        return array.size
    }
    fun updateArray(array:MutableList<String>){
        this.array = array
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: addCityViewHolder, position: Int) {
        // it is acountry name or city name

        holder.cityName.text = array[position]

        holder.cityName.setOnClickListener() {
            if(!sharedPrefArray!!.contains(array[position])) {
                sharedPrefArray?.add(array[position])
                sharePref.setValueOrNull("city", sharedPrefArray)
                context.startActivity(intent)

            }
            else{
                Toast.makeText(context,"city already exits",Toast.LENGTH_SHORT).show()
            }

        }


    }



}