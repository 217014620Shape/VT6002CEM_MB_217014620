package com.example.mobile_217014620

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.*
import com.google.firebase.database.*

data class cmt(val name: String, val cmt: String)

class DetailListActivity : AppCompatActivity() {
    private var name: String = ""
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

//    private lateinit var database: DatabaseReference

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
            val position = extras.getString("position")
//            val photo = extras.getInt("photo")
            placeX = extras.getString("placeX")!!
            placeY = extras.getString("placeY")!!

            this.findViewById<TextView>(R.id.value_name).text = name
            this.findViewById<TextView>(R.id.value_location).text = location
            this.findViewById<TextView>(R.id.value_category).text = category

            val layout = findViewById<View>(R.id.galleryShopImage) as LinearLayout
            val layoutParams = LinearLayout.LayoutParams(800, 500)
            var shopPhotos: Array<Int> = arrayOf()
            when (position) {
                "0" -> {shopPhotos = shopPhotosA}
                "1" -> {shopPhotos = shopPhotosB}
                "2" -> {shopPhotos = shopPhotosC}
                "3" -> {shopPhotos = shopPhotosD}
                "4" -> {shopPhotos = shopPhotosE}
                "5" -> {shopPhotos = shopPhotosF}
            }
            for (i in shopPhotos.indices) {
                val imageView = ImageView(this)
                imageView.id = i
                imageView.setPadding(2, 2, 2, 2)
                imageView.layoutParams = layoutParams;
                imageView.setImageBitmap(BitmapFactory.decodeResource(resources, shopPhotos[i]))
                layout.addView(imageView)
            }
        }

        val comment: EditText = this.findViewById(R.id.commentEdit)
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("shopList").child("comment")

        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    if(ds.key == "shop1"){
                        lastCMT = ds.childrenCount.toString()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("DetailListActivity", "Failed to read value.", error.toException())
            }
        })

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
    fun commentSubmit(view: View){
        val comment: EditText = this.findViewById(R.id.commentEdit)
        val txt: String = ""+comment.text
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("shopList")
        Log.d("DetailListActivity", "$"+comment.text)
        Log.d("DetailListActivity", "lastCMT => $lastCMT")
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val database = FirebaseDatabase.getInstance().getReference("shopList")
                insertCMT("userID", txt, database)
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("DetailListActivity", "false")
            }
        })
    }
    fun insertCMT(name: String, cmt: String, database: DatabaseReference) {
        val newCMT = cmt(name, cmt)
        database.child("comment").child("shop1").child(lastCMT).setValue(newCMT)
    }

}