package com.example.transportproject

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var descriptionText: TextView
    private lateinit var descriptionImage: ImageView
    private lateinit var nextArrow: ImageButton
    private lateinit var getStartedBtn: Button

    private val descriptions = arrayOf(
        "Welcome to Schneider Logistics Transport App",
        "Seamless transport management for industries",
        "Track and manage shipments efficiently",
        "Reliable transport solutions for your business"
    )

    private val images = arrayOf(
        R.drawable.truck,
        R.drawable.ship,
        R.drawable.track,
        R.drawable.business
    )

    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref: SharedPreferences = getSharedPreferences("UserRole", MODE_PRIVATE)
        val savedRole: String? = sharedPref.getString("role", null)

        if (!savedRole.isNullOrEmpty()) {
            val nextActivity = when (savedRole) {
                "Transporter" -> TransporterDashboard::class.java
                "Sender" -> SenderDashboard::class.java
                else -> RoleSelection::class.java
            }
            startActivity(Intent(this, nextActivity))
            finish()
            return
        }

        setContentView(R.layout.activity_main)

        descriptionText = findViewById(R.id.descriptionText)
        descriptionImage = findViewById(R.id.descriptionImage)
        nextArrow = findViewById(R.id.nextArrow)
        getStartedBtn = findViewById(R.id.getStartedBtn)

        nextArrow.setOnClickListener {
            if (currentIndex < descriptions.size - 1) {
                currentIndex++
                descriptionText.text = descriptions[currentIndex]
                descriptionImage.setImageResource(images[currentIndex])
            }

            if (currentIndex == descriptions.size - 1) {
                getStartedBtn.visibility = View.VISIBLE
                nextArrow.visibility = View.GONE
            }
        }

        getStartedBtn.setOnClickListener {
            startActivity(Intent(this, RoleSelection::class.java))
            finish()
        }
    }
}
