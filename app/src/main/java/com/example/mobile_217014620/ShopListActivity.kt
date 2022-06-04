package com.example.mobile_217014620

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

data class Shop(val name: String, val location: String, val category: String, val photo: Int, val placeX: String, val placeY: String)

class ShopListActivity : AppCompatActivity() {

    val nameList: MutableList<String?> = ArrayList()
    val locationList: MutableList<String?> = ArrayList()
    val categoryList: MutableList<String?> = ArrayList()
    val imgList: MutableList<String?> = ArrayList()
    val placeXList: MutableList<String?> = ArrayList()
    val placeYList: MutableList<String?> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page_layout)

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("shopList")

        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    val name = ds.child("name").getValue(String::class.java)
                    val location = ds.child("location").getValue(String::class.java)
                    val category = ds.child("category").getValue(String::class.java)
                    val img = ds.child("img").getValue(String::class.java)
                    val placeX = ds.child("placeX").getValue(String::class.java)
                    val placeY = ds.child("placeY").getValue(String::class.java)
                    nameList.add(name)
                    locationList.add(location)
                    categoryList.add(category)
                    imgList.add(img)
                    placeXList.add(placeX)
                    placeYList.add(placeY)
                }
                step2(nameList, locationList, categoryList, placeXList, placeYList)
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("DetailListActivity", "Failed to read value.", error.toException())
            }
        })
    }

    fun step2(nameList: MutableList<String?>, locationList: MutableList<String?>, categoryList: MutableList<String?>, placeXList: MutableList<String?>, placeYList: MutableList<String?>){
        val shopPhoto: Array<Int> = arrayOf(
            R.drawable.a1,
            R.drawable.b1,
            R.drawable.c1,
            R.drawable.d1,
            R.drawable.e1,
        )
        val shopList = ArrayList<Shop>()

        for (i in 0 until nameList.size) {
            val s = Shop(""+nameList[i], ""+locationList[i], ""+categoryList[i], shopPhoto[i], ""+placeXList[i], ""+placeYList[i])
            shopList.add(s)
        }

        val listView: ListView = this.findViewById(R.id.listViewComplex)
        val listAdapter = ShopAdapter(this, R.layout.home_page_item_list, shopList)
        listView.adapter = listAdapter
        listView.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(this, "Go ${shopList[position].name}", Toast.LENGTH_LONG).show()
            val intent = Intent(this, DetailListActivity::class.java)
            intent.putExtra("name", shopList[position].name)
            intent.putExtra("location", shopList[position].location)
            intent.putExtra("category", shopList[position].category)
            intent.putExtra("photo", shopList[position].photo)
            intent.putExtra("placeX", shopList[position].placeX)
            intent.putExtra("placeY", shopList[position].placeY)
            startActivity(intent)
        }
    }

    fun openAllLocation(view: View){
        val intent = Intent(this, MapsActivity::class.java)
        intent.putExtra("length", nameList.size)
        val names: Array<String?> = nameList.toTypedArray()
        intent.putExtra("names", names)
        val placeXs: Array<String?> = placeXList.toTypedArray()
        intent.putExtra("placeXs", placeXs)
        val placeYs: Array<String?> = placeYList.toTypedArray()
        intent.putExtra("placeYs", placeYs)
        startActivity(intent)
    }
}

class ShopAdapter(context: Context, resource: Int, objects: MutableList<Shop>) : ArrayAdapter<Shop>(context, resource, objects) {
    private var resource = resource
    private var shops = objects
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var v = convertView
        if (v == null){
            val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            v = layoutInflater.inflate(resource, parent, false)
        }
        val imageView = v!!.findViewById<ImageView>(R.id.imageView)
        val textViewName = v!!.findViewById<TextView>(R.id.textViewName)
        val textViewLocation = v!!.findViewById<TextView>(R.id.textViewLocation)
        val textViewCategory = v!!.findViewById<TextView>(R.id.textViewCategory)
        imageView.setImageResource(shops[position].photo)
        textViewName.text = shops[position].name
        textViewLocation.text = shops[position].location
        textViewCategory.text = shops[position].category
        return v!!
    }
}

