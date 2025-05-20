package com.example.transportproject

data class Order(
    val shipmentId: String,
    val senderName: String,
    val receiverName: String,
    val shipmentType: String,
    val deliveryDate: String,
    var status: String
)
