package com.example.transportproject

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject

class View_orders_activity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var transporterOrderAdapter: TransporterOrderAdapter
    private lateinit var orderPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_orders)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        orderPrefs = getSharedPreferences("orders", Context.MODE_PRIVATE)

        val ordersList = getAllOrders()
        transporterOrderAdapter = TransporterOrderAdapter(ordersList, object : TransporterOrderAdapter.OnOrderActionListener {
            override fun onAcceptOrder(orderId: String) {
                updateOrderStatus(orderId, "Accepted")
            }

            override fun onDeclineOrder(orderId: String) {
                updateOrderStatus(orderId, "Declined")
            }
        })
        recyclerView.adapter = transporterOrderAdapter
    }

    private fun getAllOrders(): List<Order> {
        val orders = mutableListOf<Order>()
        val orderJsonArray = JSONArray(orderPrefs.getString("order_list", "[]"))

        for (i in 0 until orderJsonArray.length()) {
            val obj = orderJsonArray.getJSONObject(i)
            val shipmentId = obj.optString("shipmentId")
            val senderName = obj.optString("senderName")
            val receiverName = obj.optString("receiverName")
            val shipmentType = obj.optString("shipmentType")
            val deliveryDate = obj.optString("deliveryDate")
            val status = obj.optString("status")
            val order = Order(shipmentId, senderName, receiverName, shipmentType, deliveryDate, status)
            orders.add(order)
        }

        return orders
    }

    private fun updateOrderStatus(orderId: String, status: String) {
        val orderJsonArray = JSONArray(orderPrefs.getString("order_list", "[]"))
        for (i in 0 until orderJsonArray.length()) {
            val obj = orderJsonArray.getJSONObject(i)
            if (obj.optString("shipmentId") == orderId) {
                obj.put("status", status)
                break
            }
        }

        orderPrefs.edit().putString("order_list", orderJsonArray.toString()).apply()
        transporterOrderAdapter.updateOrders(getAllOrders())
    }
}
