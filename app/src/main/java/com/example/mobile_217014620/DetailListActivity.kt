package com.example.mobile_217014620

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class DetailListActivity : AppCompatActivity() {
    private var name: String = ""
    private var location: String = ""
    private var category: String = ""
    private var placeX: String = ""
    private var placeY: String = ""
    private var position: String = ""
    private var lastCMT: String = ""

    private var shopPhotosA: Array<Int> = arrayOf(
        R.drawable.a1,
        R.drawable.a2,
        R.drawable.a3,
    )
    private var shopPhotosB: Array<Int> = arrayOf(
        R.drawable.b1,
        R.drawable.b2,
        R.drawable.b3,
    )
    private var shopPhotosC: Array<Int> = arrayOf(
        R.drawable.c1,
        R.drawable.c2,
    )
    private var shopPhotosD: Array<Int> = arrayOf(R.drawable.d1)
    private var shopPhotosE: Array<Int> = arrayOf(
        R.drawable.e1,
        R.drawable.e2,
        R.drawable.e3,
    )
    private var shopPhotosF: Array<Int> = arrayOf(
        R.drawable.f1,
        R.drawable.f2,
        R.drawable.f3,
    )

    private var nameList: MutableList<String?> = ArrayList()
    private var txtList: MutableList<String?> = ArrayList()
    private var shopPhotos: MutableList<String?> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        assert(supportActionBar != null)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.detail_page_layout)

        // get data from previous page
        val extras = intent.extras
        if (extras != null) {
            name = extras.getString("name").toString()
            location = extras.getString("location").toString()
            category = extras.getString("category").toString()
            position = extras.getString("position").toString()
//            val photo = extras.getInt("photo")
            placeX = extras.getString("placeX").toString()
            placeY = extras.getString("placeY").toString()

            // get data from previous page - set up text
            this.findViewById<TextView>(R.id.value_name).text = name
            this.findViewById<TextView>(R.id.value_location).text = location
            this.findViewById<TextView>(R.id.value_category).text = category

            // get data from previous page - set up image
            val layout = findViewById<View>(R.id.galleryShopImage) as LinearLayout
            val layoutParams = LinearLayout.LayoutParams(800, 500)

//            when (position) {
//                "0" -> {shopPhotos = shopPhotosA}
//                "1" -> {shopPhotos = shopPhotosB}
//                "2" -> {shopPhotos = shopPhotosC}
//                "3" -> {shopPhotos = shopPhotosD}
//                "4" -> {shopPhotos = shopPhotosE}
//                "5" -> {shopPhotos = shopPhotosF}
//            }
            val positionINT : Int = position.toInt()+1
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("shopList").child("shop$positionINT").child("img")
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for ((i, ds) in dataSnapshot.children.withIndex()) {
                        val link: String = ds.getValue(String::class.java).toString()
                        shopPhotos.add(link)
                    }
                    for (i in shopPhotos.indices) {
                        val imageView = ImageView(this@DetailListActivity)
                        imageView.id = i
                        imageView.setPadding(2, 2, 2, 2)
                        imageView.layoutParams = layoutParams;
                        Picasso.get().load(shopPhotos[i]).into(imageView)
                        layout.addView(imageView)
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // we are showing that error message in toast
                    Log.d("DetailListActivity", "cancel")
                }
            })
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
        Log.d("DetailListActivity", "length => 1")
        Log.d("DetailListActivity", "names => $name")
        Log.d("DetailListActivity", "placeXs => $placeX")
        Log.d("DetailListActivity", "placeYs => $placeY")

        intent.putExtra("length", 1)
        intent.putExtra("names", arrayOf(name))
        intent.putExtra("placeXs", arrayOf(placeX))
        intent.putExtra("placeYs", arrayOf(placeY))
        startActivity(intent)
    }
    fun openCMT(view: View){
        val positionINT : Int = position.toInt()+1
        val intent = Intent(this, CommentActivity::class.java)
        intent.putExtra("shop", "shop$positionINT")
        intent.putExtra("name", name)
        startActivity(intent)
    }
}