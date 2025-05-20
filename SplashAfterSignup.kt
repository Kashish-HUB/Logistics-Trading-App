package com.example.transportproject

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashAfterSignup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_after_signup)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}
