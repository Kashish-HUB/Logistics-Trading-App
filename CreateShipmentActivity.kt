package com.example.transportproject

import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class CreateShipmentActivity : AppCompatActivity() {

    private lateinit var senderName: EditText
    private lateinit var senderAddress: EditText
    private lateinit var senderContact: EditText
    private lateinit var receiverName: EditText
    private lateinit var receiverAddress: EditText
    private lateinit var receiverContact: EditText
    private lateinit var shipmentType: Spinner
    private lateinit var deliveryDate: TextView
    private lateinit var selectDateButton: Button
    private lateinit var shipmentImage: ImageView
    private lateinit var selectImageButton: Button
    private lateinit var createShipmentButton: Button

    private lateinit var sharedPreferences: SharedPreferences
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_shipment)

        sharedPreferences = getSharedPreferences("Shipments", MODE_PRIVATE)

        senderName = findViewById(R.id.senderName)
        senderAddress = findViewById(R.id.senderAddress)
        senderContact = findViewById(R.id.senderContact)
        receiverName = findViewById(R.id.receiverName)
        receiverAddress = findViewById(R.id.receiverAddress)
        receiverContact = findViewById(R.id.receiverContact)
        shipmentType = findViewById(R.id.shipmentType)
        deliveryDate = findViewById(R.id.deliveryDate)
        selectDateButton = findViewById(R.id.selectDateButton)
        shipmentImage = findViewById(R.id.shipmentImage)
        selectImageButton = findViewById(R.id.selectImageButton)
        createShipmentButton = findViewById(R.id.createShipmentButton)

        val shipmentOptions = arrayOf("Documents", "Electronics", "Clothing", "Furniture", "Others")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, shipmentOptions)
        shipmentType.adapter = adapter

        selectDateButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, day ->
                deliveryDate.text = "$day/${month + 1}/$year"
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
            startActivityForResult(intent, 100)
        }

        createShipmentButton.setOnClickListener {
            saveShipment()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            imageUri = data?.data
            shipmentImage.setImageURI(imageUri)
        }
    }

    private fun saveShipment() {
        if (senderName.text.isEmpty() || senderAddress.text.isEmpty() || senderContact.text.isEmpty() ||
            receiverName.text.isEmpty() || receiverAddress.text.isEmpty() || receiverContact.text.isEmpty() ||
            deliveryDate.text.isEmpty()
        ) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val shipmentId = UUID.randomUUID().toString()

        val shipmentData = "${senderName.text}|${senderAddress.text}|${senderContact.text}|" +
                "${receiverName.text}|${receiverAddress.text}|${receiverContact.text}|" +
                "${shipmentType.selectedItem}|${deliveryDate.text}|${imageUri?.toString()}|Pending"

        sharedPreferences.edit().putString(shipmentId, shipmentData).apply()

        val shipment = Shipment(
            shipmentId = shipmentId,
            senderName = senderName.text.toString(),
            senderAddress = senderAddress.text.toString(),
            senderContact = senderContact.text.toString(),
            receiverName = receiverName.text.toString(),
            receiverAddress = receiverAddress.text.toString(),
            receiverContact = receiverContact.text.toString(),
            shipmentType = shipmentType.selectedItem.toString(),
            deliveryDate = deliveryDate.text.toString(),
            imageUri = imageUri?.toString(),
            status = "Pending"
        )

        saveToTransporterOrders(shipment)

        Toast.makeText(this, "Shipment Created!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, TrackShipmentActivity::class.java))
        finish()
    }

    private fun saveToTransporterOrders(shipment: Shipment) {
        val orderPrefs = getSharedPreferences("orders", MODE_PRIVATE)
        val orderListJson = orderPrefs.getString("order_list", "[]")
        val orderArray = JSONArray(orderListJson)

        val orderJson = JSONObject()
        orderJson.put("shipmentId", shipment.shipmentId)
        orderJson.put("senderName", shipment.senderName)
        orderJson.put("receiverName", shipment.receiverName)
        orderJson.put("shipmentType", shipment.shipmentType)
        orderJson.put("deliveryDate", shipment.deliveryDate)
        orderJson.put("status", shipment.status)

        orderArray.put(orderJson)

        orderPrefs.edit().putString("order_list", orderArray.toString()).apply()
    }
}
