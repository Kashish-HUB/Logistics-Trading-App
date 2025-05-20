package com.example.transportproject

object ChatbotResponse {
    fun getResponse(query: String): String {
        return when {
            query.contains("track", ignoreCase = true) -> "You can track your shipment in the 'Track Shipment' section."
            query.contains("contact", ignoreCase = true) -> "You can call or email our support team."
            query.contains("delayed", ignoreCase = true) -> "Check the tracking page for updates, or contact support."
            query.contains("refund", ignoreCase = true) -> "Please contact support for refund-related queries."
            else -> "I'm sorry, I don't understand. Please contact support for further assistance."
        }
    }
}
