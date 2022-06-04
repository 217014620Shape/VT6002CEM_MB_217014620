package com.example.mobile_217014620

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailListActivity : AppCompatActivity() {
    private var name: String = ""
    private var placeX: String = ""
    private var placeY: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        assert(supportActionBar != null)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.detail_page_layout)

        val extras = intent.extras
        if (extras != null) {
            name = extras.getString("name")!!
            val location = extras.getString("location")
            val category = extras.getString("category")
            val photo = extras.getInt("photo")
            placeX = extras.getString("placeX")!!
            placeY = extras.getString("placeY")!!

            this.findViewById<TextView>(R.id.value_name).text = name
            this.findViewById<TextView>(R.id.value_location).text = location
            this.findViewById<TextView>(R.id.value_category).text = category
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
    fun uploadImage(view: View){
        startActivity(Intent(this, CameraActivity::class.java))
    }
    fun openMap(view: View){
        val intent = Intent(this, MapsActivity::class.java)
        intent.putExtra("length", 1)
        intent.putExtra("names", arrayOf(name))
        intent.putExtra("placeXs", arrayOf(placeX))
        intent.putExtra("placeYs", arrayOf(placeY))
        startActivity(intent)
    }
}