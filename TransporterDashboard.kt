package com.example.transportproject

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TransporterDashboard : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var trackShipment: CardView
    private lateinit var shipmentCardView: CardView
    private lateinit var noShipmentsText: TextView
    private lateinit var shipmentsRecyclerView: RecyclerView
    private lateinit var shipmentAdapter: ShipmentAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transporter_dashboard)

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        val greetingText: TextView = findViewById(R.id.greetingText1)
        val userName = sharedPreferences.getString("name", "User")
        greetingText.text = "Hi, $userName!"

        val changeRoleText: TextView = findViewById(R.id.changeRoleText1)
        val ratingBar: RatingBar = findViewById(R.id.ratingBar1)
        val ratingMessage: TextView = findViewById(R.id.ratingMessage1)
        val submitRatingButton: Button = findViewById(R.id.submitRatingButton1)
        val logisticsCardView: CardView = findViewById(R.id.logisticsCardView1)
        val profileCard: CardView = findViewById(R.id.profileCard1)
        val vehicledetals: CardView = findViewById(R.id.vehicle_details)
        val vieworders: CardView = findViewById(R.id.vieworders)
        val helpSupportCard1: CardView=findViewById(R.id.helpSupportCard1)
val meetUsCard: CardView=findViewById(R.id.meetUsCard)
        meetUsCard.setOnClickListener{
            startActivity(Intent(this, Manage_Shipment::class.java))
        }
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView1)
        toolbar = findViewById(R.id.transporterToolbar)
        shipmentCardView = findViewById(R.id.shipmentCardView1)
        noShipmentsText = findViewById(R.id.noShipmentsText1)
        shipmentsRecyclerView = findViewById(R.id.recentShipmentsRecyclerView1)
        val mapImageView1=findViewById<ImageView>(R.id.mapImageView1)
        shipmentsRecyclerView.layoutManager = LinearLayoutManager(this)
        shipmentAdapter = ShipmentAdapter(ShipmentRepository.shipments)
        shipmentsRecyclerView.adapter = shipmentAdapter
        mapImageView1.setOnClickListener{
            startActivity(Intent(this, Vehicle_Maintainance::class.java))
        }
        helpSupportCard1.setOnClickListener{
            startActivity(Intent(this, HelpSupportActivity::class.java))
        }

        vehicledetals.setOnClickListener {
            startActivity(Intent(this, landing_vd::class.java))
        }

        profileCard.setOnClickListener {
            val isSignedIn = sharedPreferences.getBoolean("isSignedIn", false)
            val intent = if (isSignedIn) Intent(this, ProfileActivity::class.java)
            else Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
        val helpSupportCard = findViewById<CardView>(R.id.helpSupportCard1)
        helpSupportCard.setOnClickListener {
            val intent = Intent(this, HelpSupportActivity::class.java)
            startActivity(intent)
        }


        vieworders.setOnClickListener {
            startActivity(Intent(this, View_orders_activity::class.java))
        }

        findViewById<CardView>(R.id.extraCardView1).setOnClickListener {
            startActivity(Intent(this, View_orders_activity::class.java))
        }

        logisticsCardView.setOnClickListener {
            startActivity(Intent(this, NearbyLogisticsActivity::class.java))
        }

        changeRoleText.setOnClickListener {
            getSharedPreferences("UserRole", MODE_PRIVATE).edit().remove("role").apply()
            val intent = Intent(this, RoleSelection::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        submitRatingButton.isEnabled = false

        val savedRating = sharedPreferences.getFloat("user_rating", 0f)
        if (savedRating > 0f) {
            ratingBar.rating = savedRating
            ratingMessage.text = getRatingMessage(savedRating)
            ratingMessage.visibility = View.VISIBLE
            submitRatingButton.isEnabled = true
        }

        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            ratingMessage.text = getRatingMessage(rating)
            ratingMessage.visibility = View.VISIBLE
            submitRatingButton.isEnabled = true
        }

        submitRatingButton.setOnClickListener {
            val selectedRating = ratingBar.rating
            Toast.makeText(this, "You rated $selectedRating stars!", Toast.LENGTH_SHORT).show()
            sharedPreferences.edit().putFloat("user_rating", selectedRating).apply()
        }

        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            drawerLayout.closeDrawer(GravityCompat.START)
            when (menuItem.itemId) {
                R.id.nav_home -> Toast.makeText(this, "You are already on Home", Toast.LENGTH_SHORT).show()
                R.id.nav_view_orders -> startActivity(Intent(this, View_orders_activity::class.java))
                R.id.nav_vehicle_details -> startActivity(Intent(this, landing_vd::class.java))
                R.id.nav_notifications -> {
                    showSimpleNotification()
                }

                R.id.nav_support -> startActivity(Intent(this, HelpSupportActivity::class.java))
                R.id.nav_rate_us -> {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Rate Us")
                    builder.setMessage("We would love to hear your feedback. Please rate us!")

                    builder.setPositiveButton("Rate Now") { _, _ ->
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
                        startActivity(intent)
                    }

                    builder.setNegativeButton("Later") { dialog, _ ->
                        dialog.dismiss()
                    }

                    builder.setNeutralButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }

                    builder.show()
                }
            }
            true
        }

    }
    private fun showSimpleNotification() {
        val channelId = "default_channel"
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "General Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "General updates and alerts"
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notifications) // Use your app's notification icon
            .setContentTitle("Notifications Enabled")
            .setContentText("You will now receive all important notifications.")
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
    override fun onResume() {
        super.onResume()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun getRatingMessage(rating: Float): String {
        return when (rating) {
            1f -> "We are sorry! We'll improve. 😞"
            2f -> "Thanks! We'll do better. 🙂"
            3f -> "Good! We appreciate it. 😊"
            4f -> "Great! Thanks for the support. 😃"
            5f -> "Awesome! We're glad you love it. 🎉"
            else -> ""
        }
    }
}
