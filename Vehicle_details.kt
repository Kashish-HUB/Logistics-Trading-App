package com.example.transportproject

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class Vehicle_details : AppCompatActivity() {

    private lateinit var vehicleImage: ImageView
    private lateinit var selectImageButton: Button
    private var imageUri: Uri? = null

    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_details)

        val driverName = findViewById<EditText>(R.id.driverName)
        val driverContact = findViewById<EditText>(R.id.driverContact)
        val vehicleNumber = findViewById<EditText>(R.id.vehicleNumber)
        val vehicleModel = findViewById<EditText>(R.id.vehicleModel)
        val vehicleType = findViewById<EditText>(R.id.vehicleType)
        val loadCapacity = findViewById<EditText>(R.id.loadCapacity)
        val saveBtn = findViewById<Button>(R.id.saveVehicleDetailsButton)

        vehicleImage = findViewById(R.id.vehicleImage)
        selectImageButton = findViewById(R.id.selectVehicleImageButton)

        selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        saveBtn.setOnClickListener {
            val newVehicle = VehicleData(
                driverName = driverName.text.toString(),
                driverContact = driverContact.text.toString(),
                vehicleNumber = vehicleNumber.text.toString(),
                vehicleModel = vehicleModel.text.toString(),
                vehicleType = vehicleType.text.toString(),
                loadCapacity = loadCapacity.text.toString(),
                imageUri = imageUri?.toString()
            )

            saveVehicleData(this, newVehicle)
            startActivity(Intent(this, Exsisting_Vehicle_Details::class.java))
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
            vehicleImage.setImageURI(imageUri)
        }
    }

    private fun saveVehicleData(context: Context, vehicleData: VehicleData) {
        val sharedPref = context.getSharedPreferences("VehicleData", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        val gson = Gson()
        val existingVehicleListJson = sharedPref.getString("vehicleList", "[]")
        val type = object : TypeToken<MutableList<VehicleData>>() {}.type
        val existingVehicleList: MutableList<VehicleData> = gson.fromJson(existingVehicleListJson, type)
        existingVehicleList.add(vehicleData)
        val updatedVehicleListJson = gson.toJson(existingVehicleList)
        editor.putString("vehicleList", updatedVehicleListJson)
        editor.apply()
    }
}
