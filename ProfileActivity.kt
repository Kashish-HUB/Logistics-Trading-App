package com.example.transportproject

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        val name = sharedPref.getString("name", "")
        val email = sharedPref.getString("email", "")
        val phone = sharedPref.getString("phone", "")
        val gender = sharedPref.getString("gender", "")
        val dob = sharedPref.getString("dob", "")
        val profession = sharedPref.getString("profession", "")
        val imageUri = sharedPref.getString("imageUri", null)

        findViewById<TextView>(R.id.textName).text = name
        findViewById<TextView>(R.id.textEmail).text = email
        findViewById<TextView>(R.id.textPhone).text = phone
        findViewById<TextView>(R.id.textGender).text = gender
        findViewById<TextView>(R.id.textDob).text = dob
        findViewById<TextView>(R.id.textProfession).text = profession

        val imageView = findViewById<ImageView>(R.id.profileImage)
        if (imageUri != null) {
            imageView.setImageURI(Uri.parse(imageUri))
        }

        val logoutBtn = findViewById<Button>(R.id.btnLogout)
        logoutBtn.setOnClickListener {
            sharedPref.edit().clear().apply()
            val intent = Intent(this, SignupActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
