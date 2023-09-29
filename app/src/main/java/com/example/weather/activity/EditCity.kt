package com.example.weather.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.adapter.currentLocationAdapter
import com.example.weather.adapter.editCityAdapter
import com.example.weather.utils.SharedPrefs

class EditCity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_city)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sharePref = SharedPrefs.getInstence(this)
        var array = sharePref.getValue("city")?.toMutableList()



        if (array != null) {
            val recyclerViewCurrentLocations = findViewById<RecyclerView>(R.id.recyclerViewEditCity)
            val adapter = editCityAdapter(this,array)
            recyclerViewCurrentLocations.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
            recyclerViewCurrentLocations.adapter = adapter

        }
    }
}