package com.example.transportproject

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class landing_vd : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_landing_vd)
        val cardAddVehicle=findViewById<CardView>(R.id.cardAddVehicle)
        val cardExsistingVehicles=findViewById<CardView>(R.id.cardExistingVehicles)
        cardAddVehicle.setOnClickListener{
            startActivity(Intent(this, Vehicle_details::class.java))
        }
        cardExsistingVehicles.setOnClickListener{
            startActivity(Intent(this,Exsisting_Vehicle_Details::class.java))
        }
    }
}