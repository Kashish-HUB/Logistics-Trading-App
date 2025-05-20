package com.example.transportproject

data class Shipment(
    val shipmentId: String,
    val senderName: String,
    val senderAddress: String,
    val senderContact: String,
    val receiverName: String,
    val receiverAddress: String,
    val receiverContact: String,
    val shipmentType: String,
    val deliveryDate: String,
    val imageUri: String?,
    var status: String
)

