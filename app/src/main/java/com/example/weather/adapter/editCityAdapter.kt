package com.example.weather.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.utils.SharedPrefs

class editCityAdapter(val context: Context, var array:MutableList<String>):
    RecyclerView.Adapter<editCityAdapter.editCityViewHolder>()  {
    val sharePref = SharedPrefs.getInstence(context)
    inner class editCityViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val name = itemview.findViewById<TextView>(R.id.textViewEditCityName)
        val btnDelete = itemview.findViewById<ImageButton>(R.id.imageButtonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): editCityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_edit_city, parent, false)
        return editCityViewHolder(view)
    }

    override fun getItemCount(): Int {
        return array.size
    }

    fun updateSharedPref(){
        sharePref.setValueOrNull("city",this.array)
    }
    fun updateArray(array:MutableList<String>){
        this.array = array
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: editCityViewHolder, position: Int) {
        holder.name.text = array[position]
        holder.btnDelete.setImageResource(R.drawable.icon_baseline_delete_outline_24)
        holder.btnDelete.setOnClickListener(){
            var temp = array
            temp.remove(array[position])
            updateArray(temp)
            sharePref.setValueOrNull("city",temp)
        }



    }

}