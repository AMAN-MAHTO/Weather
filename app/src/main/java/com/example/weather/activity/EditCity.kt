package com.example.weather.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weather.R

class EditCity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_city)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}