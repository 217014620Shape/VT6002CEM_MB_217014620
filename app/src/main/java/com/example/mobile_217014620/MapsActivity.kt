package com.example.mobile_217014620

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    var currentLocation : Location? = null
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    val REQUEST_CODE = 101

    private var length: Int = 0
    private var names: Array<String>? = arrayOf()
    private var placeXs: Array<String>? = arrayOf()
    private var placeYs: Array<String>? = arrayOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.maps_page_layout)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()

        val extras = intent.extras
        if (extras != null) {
            length = extras.getInt("length")
            names = extras.getStringArray("names")
            placeXs = extras.getStringArray("placeXs")
            placeYs = extras.getStringArray("placeYs")
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
            return
        }

        val task = fusedLocationProviderClient!!.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null){
                currentLocation = location
                val supportMapFragment = (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)
                supportMapFragment!!.getMapAsync(this@MapsActivity)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {

        if (names != null) {
            if (placeXs != null) {
                if (placeYs != null) {
                    for (i in 0..length-1) {
                        val x = placeXs!![i].toDouble()
                        val y = placeYs!![i].toDouble()
                        val shop = LatLng(x, y)
                        googleMap.addMarker(MarkerOptions().position(shop).title(names!![i]))
                    }
                }
            }
        }
        val latLng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
        val markerOptions = MarkerOptions().position(latLng).title("I Am Here!")
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10f))
        googleMap.addMarker(markerOptions)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            REQUEST_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    fetchLocation()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}