package com.example.mobile_217014620

import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.*
import com.google.firebase.database.*

data class cmt(val name: String, val txt: String)

class CommentActivity : AppCompatActivity() {

    private var shopName: String = ""
    private var name: String = ""
    private var nameList: MutableList<String?> = ArrayList()
    private var txtList: MutableList<String?> = ArrayList()
    private var lastCMT: String = ""
    private var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        assert(supportActionBar != null)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.comment_page_layout)

        val extras = intent.extras
        if (extras != null) {
            shopName = extras.getString("shop").toString()
            name = extras.getString("name").toString()
        }
        this.findViewById<ImageView>(R.id.imageView_cmt).setImageResource(R.drawable.cmt)
        this.findViewById<TextView>(R.id.shop).text = name
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("shopList").child("comment")

        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                nameList.clear()
                txtList.clear()
                for (ds in dataSnapshot.children) {
                    if(ds.key == shopName){
                        lastCMT = ds.childrenCount.toString()
                        for (i in 0 until ds.childrenCount){
                            val name = ds.child(i.toString()).child("name").getValue(String::class.java)
                            val txt = ds.child(i.toString()).child("txt").getValue(String::class.java)
                            nameList.add(name)
                            txtList.add(txt)
                        }
                    }
                }
                step2()
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("HKTCA", "Failed to read value.", error.toException())
            }
        })
    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
    fun commentSubmit(view: View){
        val comment: EditText = this.findViewById(R.id.commentEdit)
        val txt: String = ""+comment.text
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("shopList")
        var username: String = "someone"
        sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE)
        if (sharedPreferences!!.contains(MainActivity.USERNAME_KEY)) {
            username = sharedPreferences!!.getString(MainActivity.USERNAME_KEY, "").toString()
        }
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val database = FirebaseDatabase.getInstance().getReference("shopList")
                insertCMT(username, txt, database)
                comment.setText("")
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("HKTCA", "Failed to read value.", error.toException())
            }
        })
    }
    fun insertCMT(name: String, txt: String, database: DatabaseReference) {
        val newCMT = cmt(name, txt)
        database.child("comment").child(shopName).child(lastCMT).setValue(newCMT)
    }
    fun step2(){
        val listCMT = ArrayList<cmt>()
        for (i in 0 until nameList.size) {
            val s = cmt(""+nameList[i], ""+txtList[i])
            listCMT.add(s)
        }
        val listView: ListView = this.findViewById(R.id.listViewComplex)
        val listAdapter = CMTAdapter(this, R.layout.detail_page_item_list, listCMT)
        listView.adapter = listAdapter
        listView.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(this, "Go $position", Toast.LENGTH_LONG).show()
        }
    }
}

class CMTAdapter(context: Context, resource: Int, objects: MutableList<cmt>) : ArrayAdapter<cmt>(context, resource, objects) {
    private var resource = resource
    private var CMTObj = objects
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var v = convertView
        if (v == null){
            val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            v = layoutInflater.inflate(resource, parent, false)
        }

        val img = v!!.findViewById<ImageView>(R.id.imageView)
        val textViewName = v!!.findViewById<TextView>(R.id.textViewName)
        val textViewCMT = v!!.findViewById<TextView>(R.id.textViewCMT)
        img.setImageResource(R.drawable.cmt)
        textViewName.text = CMTObj[position].name + " say :"
        textViewCMT.text = CMTObj[position].txt
        return v!!
    }
}