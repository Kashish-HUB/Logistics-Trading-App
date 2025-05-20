package com.example.transportproject

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class SignupActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private var imageUri: Uri? = null
    private val imagePickCode = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        if (sharedPref.getBoolean("isSignedIn", false)) {
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_signup)

        imageView = findViewById(R.id.profileImage)
        val editName = findViewById<EditText>(R.id.editName)
        val editEmail = findViewById<EditText>(R.id.editEmail)
        val editPhone = findViewById<EditText>(R.id.editPhone)
        val editPassword = findViewById<EditText>(R.id.editPassword)
        val genderGroup = findViewById<RadioGroup>(R.id.genderGroup)
        val dobText = findViewById<TextView>(R.id.dobText)
        val spinnerProfession = findViewById<Spinner>(R.id.spinnerProfession)
        val btnSignup = findViewById<Button>(R.id.btnSignup)

        val professions = arrayOf("Select Profession", "Student", "Driver", "Manager", "Business Owner", "Other")
        spinnerProfession.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, professions)

        dobText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val dp = DatePickerDialog(
                this,
                { _, year, month, day ->
                    dobText.text = "$day/${month + 1}/$year"
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            dp.show()
        }

        imageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, imagePickCode)
        }

        btnSignup.setOnClickListener {
            val name = editName.text.toString()
            val email = editEmail.text.toString()
            val phone = editPhone.text.toString()
            val password = editPassword.text.toString()
            val genderId = genderGroup.checkedRadioButtonId
            val gender = findViewById<RadioButton>(genderId)?.text.toString()
            val dob = dobText.text.toString()
            val profession = spinnerProfession.selectedItem.toString()

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() ||
                genderId == -1 || dob == "Select Date" || profession == "Select Profession") {
                Toast.makeText(this, "Fill all details", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            with(sharedPref.edit()) {
                putBoolean("isSignedIn", true)
                putString("name", name)
                putString("email", email)
                putString("phone", phone)
                putString("password", password)
                putString("gender", gender)
                putString("dob", dob)
                putString("profession", profession)
                if (imageUri != null) putString("imageUri", imageUri.toString())
                apply()
            }

            startActivity(Intent(this, SplashAfterSignup::class.java))
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == imagePickCode && resultCode == RESULT_OK) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
        }
    }
}
