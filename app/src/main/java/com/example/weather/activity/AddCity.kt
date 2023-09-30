package com.example.weather.activity

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.compose.runtime.rememberCompositionContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.adapter.addCityAdapter
import com.example.weather.adapter.currentLocationAdapter
import com.example.weather.adapter.editCityAdapter
import com.example.weather.utils.SharedPrefs
import java.util.Locale
import kotlin.math.log

class AddCity : AppCompatActivity() {

    private lateinit var cityArray:ArrayList<String>
//    private lateinit var tempArray:ArrayList<String>
    private lateinit var recyclerViewCurrentLocations: RecyclerView


    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_city)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        val sharePref = SharedPrefs.getInstence(this)
        var array = sharePref.getValue("city")?.toMutableList()

        cityArray = arrayListOf("Andhra Pradesh","Arunachal Pradesh","Assam","Bihar","Chhattisgarh","Goa","Gujarat","Haryana","Himachal Pradesh","Jharkhand","Karnataka","Kerala","Madhya Pradesh","Maharashtra","Manipur","Meghalaya","Mizoram","Nagaland","Odisha","Punjab","Rajasthan","Sikkim","Tamil Nadu","Telagana","Tripura","Utta","Pradesh","Uttarakhand","West Bengal",
            "Kabul",
            "Mariehamn",
            "Anchorage","Arnold","Chicago","Dallas","Denver","Honolulu","Houston","Kingsburg","Los Angeles","Miami","New York","Palo Alto","San Antonio","San Diego","San Francisco","San Jose","San Marino","Washington")
        var countrArray = arrayListOf<String>("India","Afghanistan","Aland Islands","United States")

//        tempArray.addAll(cityArray)
        recyclerViewCurrentLocations = findViewById<RecyclerView>(R.id.recyclerViewCityList)
        val adapter = addCityAdapter(this,cityArray,countrArray)
        recyclerViewCurrentLocations.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        recyclerViewCurrentLocations.adapter = adapter

    }

    @Override
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.top_app_bar, menu)
        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        val appBarSearch = menu?.findItem(R.id.app_bar_search)
        val searchView = appBarSearch?.actionView as SearchView

        searchView.setSearchableInfo(manager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()

                searchView.onActionViewCollapsed()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                Log.d("search", "onQueryTextChange: "+newText.toString())
//                val tempArray = arrayListOf<String>()
//                if (newText != null) {
//                    if(newText.isNotEmpty()){
//                        cityArray.forEach{
//                            if(it.lowercase(Locale.getDefault()).contains(newText.lowercase(Locale.getDefault()))){
//                                tempArray.add(it)
//                            }
//                        }
//                        Log.d("search", "onQueryTextChange: "+tempArray.toString())
//                    }
//
//                }


               return false
            }

        })

        return super.onCreateOptionsMenu(menu)
    }


}