package com.example.transportproject

import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Vehicle_Maintainance : AppCompatActivity() {

    private lateinit var vehicleRecyclerView: RecyclerView
    private lateinit var vehicleAdapter: VehicleMaintenanceAdapter
    private var vehicleList: List<VehicleData> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_maintainance)

        vehicleRecyclerView = findViewById(R.id.vehicleRecyclerView)

        loadVehicleData()

        // Check if list is not empty before initializing adapter
        if (vehicleList.isNotEmpty()) {
            setupRecyclerView()
        } else {
            Toast.makeText(this, "No vehicle data found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        vehicleRecyclerView.layoutManager = LinearLayoutManager(this)
        vehicleAdapter = VehicleMaintenanceAdapter(
            vehicleList,
            onViewHistoryClick = { position -> viewHistory(position) },
            onBookMaintenanceClick = { position -> bookMaintenance(position) }
        )
        vehicleRecyclerView.adapter = vehicleAdapter
    }

    private fun loadVehicleData() {
        try {
            val sharedPref = getSharedPreferences("VehicleData", Context.MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPref.getString("vehicleList", null)
            val type = object : TypeToken<List<VehicleData>>() {}.type
            vehicleList = gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            vehicleList = emptyList()
            Toast.makeText(this, "Error loading vehicle data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun viewHistory(position: Int) {
        val vehicle = vehicleList.getOrNull(position)
        vehicle?.let {
            Toast.makeText(this, "View History for ${it.vehicleNumber}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun bookMaintenance(position: Int) {
        val vehicle = vehicleList.getOrNull(position) ?: return

        val dialogView = layoutInflater.inflate(R.layout.dialog_appointment_booking, null)
        val datePicker = dialogView.findViewById<DatePicker>(R.id.datePicker)
        val citySpinner = dialogView.findViewById<Spinner>(R.id.citySpinner)

        val cities = arrayOf(
            "Amaravati", "Itanagar", "Dispur", "Patna", "Raipur", "Panaji", "Gandhinagar", "Chandigarh",
            "Shimla", "Ranchi", "Bengaluru", "Thiruvananthapuram", "Bhopal", "Mumbai", "Imphal", "Shillong",
            "Aizawl", "Kohima", "Bhubaneswar", "Chandigarh", "Jaipur", "Gangtok", "Chennai", "Hyderabad",
            "Agartala", "Lucknow", "Dehradun", "Kolkata", "Port Blair", "Daman", "Kavaratti", "New Delhi", "Puducherry"
        )

        val cityAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cities)
        citySpinner.adapter = cityAdapter

        val builder = AlertDialog.Builder(this)
            .setTitle("Book Maintenance Appointment")
            .setView(dialogView)
            .setPositiveButton("Confirm") { dialog, _ ->
                val selectedDate = "${datePicker.dayOfMonth}/${datePicker.month + 1}/${datePicker.year}"
                val selectedCity = citySpinner.selectedItem?.toString() ?: "Unknown"

                Toast.makeText(
                    this,
                    "Your query is taken for ${vehicle.vehicleNumber} in $selectedCity on $selectedDate.\n" +
                            "The nearest maintenance center will contact you soon.",
                    Toast.LENGTH_LONG
                ).show()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        builder.create().show()
    }
}
