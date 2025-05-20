package com.example.transportproject

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.DatePicker
import android.widget.Spinner
import android.widget.ArrayAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Exsisting_Vehicle_Details : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exsisting_vehicle_details)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewVehicle)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val vehicleList = getVehicleData(this)
        val adapter = VehicleAdapter(vehicleList,
            { position -> viewHistory(position) },
            { position -> bookMaintenance(position) }
        )
        recyclerView.adapter = adapter
    }
    private fun getVehicleData(context: Context): List<VehicleData> {
        val sharedPref = context.getSharedPreferences("VehicleData", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPref.getString("vehicleList", "[]")
        return gson.fromJson(json, object : TypeToken<List<VehicleData>>() {}.type)
    }

    private fun viewHistory(position: Int) {
        Toast.makeText(this, "View History for ${position + 1}", Toast.LENGTH_SHORT).show()
    }

    private fun bookMaintenance(position: Int) {
        val vehicle = getVehicleData(this)[position]

        val dialogView = layoutInflater.inflate(R.layout.dialog_appointment_booking, null)
        val datePicker = dialogView.findViewById<DatePicker>(R.id.datePicker)
        val citySpinner = dialogView.findViewById<Spinner>(R.id.citySpinner)

        val cities = arrayOf("Amaravati", // Andhra Pradesh
            "Itanagar", // Arunachal Pradesh
            "Dispur", // Assam
            "Patna", // Bihar
            "Raipur", // Chhattisgarh
            "Panaji", // Goa
            "Gandhinagar", // Gujarat
            "Chandigarh", // Haryana
            "Shimla", // Himachal Pradesh
            "Ranchi", // Jharkhand
            "Bengaluru", // Karnataka
            "Thiruvananthapuram", // Kerala
            "Bhopal", // Madhya Pradesh
            "Mumbai", // Maharashtra
            "Imphal", // Manipur
            "Shillong", // Meghalaya
            "Aizawl", // Mizoram
            "Kohima", // Nagaland
            "Bhubaneswar", // Odisha
            "Chandigarh", // Punjab
            "Jaipur", // Rajasthan
            "Gangtok", // Sikkim
            "Chennai", // Tamil Nadu
            "Hyderabad", // Telangana
            "Agartala", // Tripura
            "Lucknow", // Uttar Pradesh
            "Dehradun", // Uttarakhand
            "Kolkata", // West Bengal
            "Port Blair", // Andaman and Nicobar Islands
            "Chandigarh", // Chandigarh
            "Daman", // Dadra and Nagar Haveli and Daman and Diu
            "Kavaratti", // Lakshadweep
            "New Delhi", // Delhi
            "Puducherry" // Puducherry
             )
        val cityAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cities)
        citySpinner.adapter = cityAdapter

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Book Maintenance Appointment")
        builder.setView(dialogView)

        builder.setPositiveButton("Confirm") { dialog, which ->
            val selectedDate = "${datePicker.dayOfMonth}/${datePicker.month + 1}/${datePicker.year}"
            val selectedCity = citySpinner.selectedItem.toString()
            Toast.makeText(
                this,
                "Your query is taken for vehicle ${vehicle.vehicleNumber} in $selectedCity on $selectedDate. " +
                        "The nearest maintenance center will contact you soon.",
                Toast.LENGTH_LONG
            ).show()

            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }
}
