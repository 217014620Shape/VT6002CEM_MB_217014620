package com.example.mobile_217014620

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.mobile_217014620.databinding.MapsPageLayoutBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: MapsPageLayoutBinding
    private var length: Int = 0
    private var names: Array<String>? = arrayOf()
    private var placeXs: Array<String>? = arrayOf()
    private var placeYs: Array<String>? = arrayOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        assert(supportActionBar != null)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val extras = intent.extras
        if (extras != null) {
            length = extras.getInt("length")
            names = extras.getStringArray("names")
            placeXs = extras.getStringArray("placeXs")
            placeYs = extras.getStringArray("placeYs")
        }

        binding = MapsPageLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (names != null) {
            if (placeXs != null) {
                if (placeYs != null) {
                    for (i in 0..length-1) {
                        val x = placeXs!![i].toDouble()
                        val y = placeYs!![i].toDouble()
                        val shop = LatLng(x, y)
                        mMap.addMarker(MarkerOptions().position(shop).title(names!![i]))
                    }
                }
            }
        }
        val make = LatLng(22.31950, 114.17013)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(make, 10f))
    }
}