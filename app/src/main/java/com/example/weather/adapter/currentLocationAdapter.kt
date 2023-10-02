package com.example.weather.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R

class currentLocationAdapter(context: Context, val array:List<String>):
    RecyclerView.Adapter<currentLocationAdapter.currentLocationViewHolder>() {

    inner class currentLocationViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val name = itemview.findViewById<TextView>(R.id.textViewCurrentLocationListName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): currentLocationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_current_location, parent, false)
        return currentLocationViewHolder(view)
    }

    override fun onBindViewHolder(holder: currentLocationViewHolder, position: Int) {
        holder.name.text = array.get(position)
    }

    override fun getItemCount(): Int {

        return array.size
    }
}