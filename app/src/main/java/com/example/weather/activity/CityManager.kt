package com.example.weather.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.adapter.currentLocationAdapter

import com.example.weather.utils.SharedPrefs

class cityManager : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_manager)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val btnAdd = findViewById<Button>(R.id.buttonAddCity)
        val btnEdit = findViewById<Button>(R.id.buttonEditCity)

        val sharePref = SharedPrefs.getInstence(this)
        var array = sharePref.getValue("city")?.toMutableList()


        if (array != null) {
            val recyclerViewCurrentLocations = findViewById<RecyclerView>(R.id.recyclerViewCurrentLocationsList)
            val adapter = currentLocationAdapter(this,array)
            recyclerViewCurrentLocations.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
            recyclerViewCurrentLocations.adapter = adapter


            sharePref.setValue("city",array)
        }

        btnAdd.setOnClickListener(){
            val intent = Intent(this,AddCity::class.java)
            startActivity(intent)
        }
        btnEdit.setOnClickListener(){
            val intent = Intent(this,EditCity::class.java)
            startActivity(intent)
        }

    }
}