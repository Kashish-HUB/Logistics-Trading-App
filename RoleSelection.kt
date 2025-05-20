package com.example.transportproject

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import android.widget.Button

class RoleSelection : AppCompatActivity() {

    private lateinit var transportCard: CardView
    private lateinit var senderCard: CardView
    private lateinit var proceedBtn: Button
    private var selectedRole: String? = null
    private lateinit var sharedPref: SharedPreferences

    companion object {
        private const val PREFS_NAME = "UserRole"
        private const val ROLE_KEY = "role"
        private const val TRANSPORTER = "Transporter"
        private const val SENDER = "Sender"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val savedRole = sharedPref.getString(ROLE_KEY, null)

        if (savedRole != null) {
            openDashboard(savedRole)
            return
        }

        setContentView(R.layout.activity_role_selection)
        transportCard = findViewById(R.id.transportCard)
        senderCard = findViewById(R.id.senderCard)
        proceedBtn = findViewById(R.id.proceedBtn)
        proceedBtn.visibility = View.GONE
        transportCard.setOnClickListener {
            selectRole(TRANSPORTER, transportCard, senderCard)
        }

        senderCard.setOnClickListener {
            selectRole(SENDER, senderCard, transportCard)
        }

        proceedBtn.setOnClickListener {
            selectedRole?.let { role ->
                // Save selected role
                with(sharedPref.edit()) {
                    putString(ROLE_KEY, role)
                    apply()
                }
                openDashboard(role)
            }
        }
    }

    private fun selectRole(role: String, selectedCard: CardView, otherCard: CardView) {
        selectedRole = role

        selectedCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.selected_card))
        otherCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.default_card))

        proceedBtn.visibility = View.VISIBLE
    }

    private fun openDashboard(role: String) {
        val intent = when (role) {
            TRANSPORTER -> Intent(this, TransporterDashboard::class.java)
            SENDER -> Intent(this, SenderDashboard::class.java)
            else -> return
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
