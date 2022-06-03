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


data class Shop(val name : String, val location : String, val category: String, val photo : Int)

class ShopListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page_layout)

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("shopList")

        val nameList: MutableList<String?> = ArrayList()
        val locationList: MutableList<String?> = ArrayList()
        val categoryList: MutableList<String?> = ArrayList()
        val imgList: MutableList<String?> = ArrayList()

        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    val name = ds.child("name").getValue(String::class.java)
                    val location = ds.child("location").getValue(String::class.java)
                    val category = ds.child("category").getValue(String::class.java)
                    val img = ds.child("img").getValue(String::class.java)
                    nameList.add(name)
                    locationList.add(location)
                    categoryList.add(category)
                    imgList.add(img)
                }
                step2(nameList, locationList, categoryList)
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("DetailListActivity", "Failed to read value.", error.toException())
            }
        })
    }

    fun step2(nameList: MutableList<String?>, locationList: MutableList<String?>, categoryList: MutableList<String?>){
        val shopPhoto: Array<Int> = arrayOf(
            R.drawable.a1,
            R.drawable.b1,
            R.drawable.c1,
            R.drawable.d1,
            R.drawable.e1,
        )
        var shopList = ArrayList<Shop>()

        @Override
        for (i in 0 until nameList.size) {
            val s = Shop(""+nameList[i], ""+locationList[i], ""+categoryList[i], shopPhoto[i])
            shopList.add(s)
        }

        val listView: ListView = this.findViewById(R.id.listViewComplex)
        val listAdapter = ShopAdapter(this, R.layout.home_page_item_list, shopList)
        listView.adapter = listAdapter
        // using lambda syntax
        listView.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(this, "You clicked ${shopList[position].name}", Toast.LENGTH_LONG).show()

            val intent = Intent(this, DetailListActivity::class.java)
            intent.putExtra("name", shopList[position].name);
            intent.putExtra("location", shopList[position].location);
            intent.putExtra("category", shopList[position].category);
            intent.putExtra("photo", shopList[position].photo);
            startActivity(intent)
        }
    }
}

//subclass of ArrayList<T>, you need to inherit the constructor as well.
class ShopAdapter(context: Context, resource: Int, objects: MutableList<Shop>) :
    ArrayAdapter<Shop>(context, resource, objects) {
    private var resource = resource
    private var shops = objects
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var v = convertView
        if (v == null){
            val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            v = layoutInflater.inflate(resource, parent, false)
        }
        var imageView = v!!.findViewById<ImageView>(R.id.imageView)
        var textViewName = v!!.findViewById<TextView>(R.id.textViewName)
        var textViewLocation = v!!.findViewById<TextView>(R.id.textViewLocation)
        var textViewCategory = v!!.findViewById<TextView>(R.id.textViewCategory)
        imageView.setImageResource(shops[position].photo)
        textViewName.text = shops[position].name
        textViewLocation.text = shops[position].location
        textViewCategory.text = shops[position].category
        return v!!
    }
}

