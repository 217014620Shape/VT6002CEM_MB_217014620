package com.example.mobile_217014620

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.IOException
import java.util.*

class CameraActivity : AppCompatActivity() {

    private lateinit var takePhoto: Button
    private lateinit var upload: Button
    private lateinit var photo: ImageView
    private val cameraRequestId = 200

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.camera_page_layout)

        takePhoto = findViewById(R.id.takePhoto)
        upload = findViewById(R.id.upload)
        photo = findViewById(R.id.photo)

        /**get Permission*/
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), cameraRequestId)
        /**set camera Open*/
        takePhoto.setOnClickListener {
            val cameraInt = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraInt, cameraRequestId)
        }

        upload.setOnClickListener {
            Toast.makeText(this, "Upload to firebase is coming soon", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("DetailListActivity", "//onActivityResult//")

        if (requestCode == cameraRequestId) {
            /**save to Image In layout*/
            val images: Bitmap = data?.extras?.get("data") as Bitmap
            photo.setImageBitmap(images)
        }
    }

}