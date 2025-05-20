package com.example.transportproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TransporterOrderAdapter(
    private var orders: List<Order>,
    private val listener: OnOrderActionListener
) : RecyclerView.Adapter<TransporterOrderAdapter.OrderViewHolder>() {

    interface OnOrderActionListener {
        fun onAcceptOrder(orderId: String)
        fun onDeclineOrder(orderId: String)
    }

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val shipmentIdText: TextView = itemView.findViewById(R.id.shipmentIdText)
        val senderNameText: TextView = itemView.findViewById(R.id.senderNameText)
        val receiverNameText: TextView = itemView.findViewById(R.id.receiverNameText)
        val shipmentTypeText: TextView = itemView.findViewById(R.id.shipmentTypeText)
        val deliveryDateText: TextView = itemView.findViewById(R.id.deliveryDateText)
        val statusText: TextView = itemView.findViewById(R.id.statusText)
        val acceptButton: Button = itemView.findViewById(R.id.acceptButton)
        val declineButton: Button = itemView.findViewById(R.id.declineButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transporter_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]

        holder.shipmentIdText.text = "ID: ${order.shipmentId}"
        holder.senderNameText.text = "Sender: ${order.senderName}"
        holder.receiverNameText.text = "Receiver: ${order.receiverName}"
        holder.shipmentTypeText.text = "Type: ${order.shipmentType}"
        holder.deliveryDateText.text = "Date: ${order.deliveryDate}"
        holder.statusText.text = "Status: ${order.status}"

        holder.acceptButton.setOnClickListener {
            listener.onAcceptOrder(order.shipmentId)
        }

        holder.declineButton.setOnClickListener {
            listener.onDeclineOrder(order.shipmentId)
        }
    }

    override fun getItemCount(): Int = orders.size

    fun updateOrders(newOrders: List<Order>) {
        orders = newOrders
        notifyDataSetChanged()
    }
}
