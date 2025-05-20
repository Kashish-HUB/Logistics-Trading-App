package com.example.transportproject

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import org.json.JSONArray

class TrackShipmentActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var shipmentAdapter: ShipmentAdapter
    private lateinit var shipmentPrefs: SharedPreferences
    private lateinit var orderPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_shipment)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        shipmentPrefs = getSharedPreferences("Shipments", MODE_PRIVATE)
        orderPrefs = getSharedPreferences("orders", Context.MODE_PRIVATE)

        val shipmentsList = getUpdatedShipments()
        shipmentAdapter = ShipmentAdapter(shipmentsList)
        recyclerView.adapter = shipmentAdapter

        saveShipmentsToUserPrefs(shipmentsList)
    }

    private fun getUpdatedShipments(): List<Shipment> {
        val shipments = mutableListOf<Shipment>()
        val allEntries = shipmentPrefs.all
        val orderJsonArray = JSONArray(orderPrefs.getString("order_list", "[]"))

        val statusMap = mutableMapOf<String, String>()
        for (i in 0 until orderJsonArray.length()) {
            val obj = orderJsonArray.getJSONObject(i)
            val shipmentId = obj.optString("shipmentId")
            val status = obj.optString("status")
            if (shipmentId.isNotEmpty()) {
                statusMap[shipmentId] = status
            }
        }

        for ((key, value) in allEntries) {
            val data = (value as? String)?.split("|")
            if (data != null && data.size >= 10) {
                val currentStatus = statusMap[key] ?: data[9]
                val shipment = Shipment(
                    shipmentId = key,
                    senderName = data[0],
                    senderAddress = data[1],
                    senderContact = data[2],
                    receiverName = data[3],
                    receiverAddress = data[4],
                    receiverContact = data[5],
                    shipmentType = data[6],
                    deliveryDate = data[7],
                    imageUri = data[8],
                    status = currentStatus
                )
                shipments.add(shipment)
            }
        }

        return shipments
    }

    private fun saveShipmentsToUserPrefs(shipments: List<Shipment>) {
        val json = Gson().toJson(shipments)
        val userPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        userPrefs.edit().putString("shipments", json).apply()
    }
}
