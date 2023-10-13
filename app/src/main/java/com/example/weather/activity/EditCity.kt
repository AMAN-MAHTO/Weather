package com.example.weather.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.DOWN
import androidx.recyclerview.widget.ItemTouchHelper.END
import androidx.recyclerview.widget.ItemTouchHelper.START
import androidx.recyclerview.widget.ItemTouchHelper.UP
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.weather.R
import com.example.weather.adapter.currentLocationAdapter
import com.example.weather.adapter.editCityAdapter
import com.example.weather.utils.SharedPrefs

class EditCity : AppCompatActivity() {

    private lateinit var adapter:editCityAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_city)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sharePref = SharedPrefs.getInstence(this)
        var array = sharePref.getValue("city")?.toMutableList()



        if (array != null) {
            val recyclerViewCurrentLocations = findViewById<RecyclerView>(R.id.recyclerViewEditCity)
            adapter = editCityAdapter(this,array)
            recyclerViewCurrentLocations.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
            recyclerViewCurrentLocations.adapter = adapter

//            itemTouchHelper.attachToRecyclerView(recyclerViewCurrentLocations)
        }
    }

    // anable to update sharedpref city data, and item moved

//    private val itemTouchHelper by lazy {
//
//        val simpleItemTouchCallBack =
//            object : ItemTouchHelper.SimpleCallback(UP or DOWN or START or END, 0){
//                override fun onMove(
//                    recyclerView: RecyclerView,
//                    viewHolder: RecyclerView.ViewHolder,
//                    target: RecyclerView.ViewHolder
//                ): Boolean {
//                    val adapter = recyclerView.adapter
//                    val from = viewHolder.adapterPosition
//                    val to = target.adapterPosition
//
//                    adapter?.notifyItemMoved(from, to)
//
//                    return true
//                }
//
//                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//
//                }
//
//            }
//        ItemTouchHelper(simpleItemTouchCallBack)
//    }



}