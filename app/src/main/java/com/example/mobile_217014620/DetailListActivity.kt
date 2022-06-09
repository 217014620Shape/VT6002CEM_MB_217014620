package com.example.mobile_217014620

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class DetailListActivity : AppCompatActivity() {
    private var name: String = ""
    private var location: String = ""
    private var category: String = ""
    private var placeX: String = ""
    private var placeY: String = ""
    private var position: String = ""
    private var lastCMT: String = ""

    private var shopPhotos: MutableList<String?> = ArrayList()
    private var uploadedPhotos: MutableList<String?> = ArrayList()

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
            val layoutU = findViewById<View>(R.id.galleryUploadImage) as LinearLayout
            val layoutParams = LinearLayout.LayoutParams(800, 500)

            val positionINT : Int = position.toInt()+1
            val database = FirebaseDatabase.getInstance()
            val shopP = database.getReference("shopList").child("shop$positionINT").child("img")
            val uploadP = database.getReference("image").child("shop$positionINT")
            shopPhotoLoading(shopP, layoutParams, layout)
            uploadedPhotoLoading(uploadP, layoutParams, layoutU)
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
    fun openCMT(view: View){
        val positionINT : Int = position.toInt()+1
        val intent = Intent(this, CommentActivity::class.java)
        intent.putExtra("shop", "shop$positionINT")
        intent.putExtra("name", name)
        startActivity(intent)
    }
    private fun shopPhotoLoading(myRef: DatabaseReference, layoutParams: LinearLayout.LayoutParams, layout: LinearLayout){
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                shopPhotos.clear()
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
                Log.w("HKTCA", "Failed to read value.")

            }
        })
    }
    private fun uploadedPhotoLoading(myRef: DatabaseReference, layoutParams: LinearLayout.LayoutParams, layout: LinearLayout){
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                uploadedPhotos.clear()
                for (ds in dataSnapshot.children) {
                    val link = ds.getValue(String::class.java)
                    uploadedPhotos.add(link)
                }
                for (i in uploadedPhotos.indices) {
                    val imageView = ImageView(this@DetailListActivity)
                    imageView.id = i
                    imageView.setPadding(2, 2, 2, 2)
                    imageView.layoutParams = layoutParams;
                    Picasso.get().load(uploadedPhotos[i]).into(imageView)
                    layout.addView(imageView)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // we are showing that error message in toast
                Log.w("HKTCA", "Failed to read value.")
            }
        })
    }
}