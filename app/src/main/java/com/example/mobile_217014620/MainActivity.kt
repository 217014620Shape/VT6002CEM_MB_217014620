package com.example.mobile_217014620

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor


private lateinit var executor: Executor
private lateinit var biometricPrompt: BiometricPrompt
private lateinit var promptInfo: BiometricPrompt.PromptInfo

class MainActivity : AppCompatActivity() {

    private var username : String = ""
    private var sharedPreferences: SharedPreferences? = null

    companion object {
        const val USERNAME_KEY  = "USERNAME_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var tvFail: TextView =  this.findViewById<TextView>(R.id.fail)
        val editTextName = this.findViewById<EditText>(R.id.username)

        sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE)
        if (sharedPreferences!!.contains(USERNAME_KEY)) {
            editTextName!!.setText(sharedPreferences!!.getString(USERNAME_KEY, ""))
        }

        executor = ContextCompat.getMainExecutor(this)

        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(applicationContext,"ERROR : $errString", Toast.LENGTH_LONG).show()
            }
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Toast.makeText(applicationContext,"Authentication succeeded!", Toast.LENGTH_SHORT).show()
                authSuccess()
            }
            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                tvFail.text = "Failed : Authentication failed. Please retry."
            }
        })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()

        val biometricLoginButton = findViewById<Button>(R.id.biometric_login)
        biometricLoginButton.setOnClickListener {
            var username: String = this.findViewById<EditText>(R.id.username).text.toString()
            if(username != ""){
                biometricPrompt.authenticate(promptInfo)
                val editor = sharedPreferences!!.edit()
                editor.putString(USERNAME_KEY, editTextName!!.text.toString())
                editor.commit()
            }else{
                tvFail.text = "Please insert username"
            }
        }
    }

    private fun authSuccess(){
        username = this.findViewById<EditText>(R.id.username).text.toString()
        val intent = Intent(this, ShopListActivity::class.java)
        intent.putExtra("username", username)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        val biometricStatusTextView = findViewById<TextView>(R.id.error)
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
//            BiometricManager.BIOMETRIC_SUCCESS -> biometricStatusTextView.text = "App can authenticate using biometrics."
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> biometricStatusTextView.text = "No biometric features available on this device."
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> biometricStatusTextView.text = "Biometric features are currently unavailable."
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> biometricStatusTextView.text = "Biometric features are not enrolled."
        }
    }
}