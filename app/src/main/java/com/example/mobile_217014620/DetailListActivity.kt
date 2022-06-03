package com.example.mobile_217014620

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_page_layout)

        val extras = intent.extras
        if (extras != null) {
            val name = extras.getString("name")
            val location = extras.getString("location")
            val category = extras.getString("category")
            val photo = extras.getInt("photo")
            Log.d("DetailListActivity", "name -> " + name);
            Log.d("DetailListActivity", "location -> " + location);
            Log.d("DetailListActivity", "category -> " + category);
            Log.d("DetailListActivity", "photo -> " + photo);

            this.findViewById<TextView>(R.id.value_name).text = name
            this.findViewById<TextView>(R.id.value_location).text = location
            this.findViewById<TextView>(R.id.value_category).text = category
        }
    }
}