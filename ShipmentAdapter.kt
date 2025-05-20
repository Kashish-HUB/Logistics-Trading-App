package com.example.transportproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ShipmentAdapter(private var shipments: List<Shipment>) : RecyclerView.Adapter<ShipmentAdapter.ShipmentViewHolder>() {

    fun updateShipments(newShipments: List<Shipment>) {
        shipments = newShipments
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShipmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shipment, parent, false)
        return ShipmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShipmentViewHolder, position: Int) {
        val shipment = shipments[position]
        holder.shipmentId.text = shipment.shipmentId
        holder.senderName.text = shipment.senderName
        holder.receiverName.text = shipment.receiverName
        holder.status.text = shipment.status
    }

    override fun getItemCount(): Int = shipments.size

    class ShipmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val shipmentId: TextView = itemView.findViewById(R.id.shipmentId)
        val senderName: TextView = itemView.findViewById(R.id.senderName)
        val receiverName: TextView = itemView.findViewById(R.id.receiverName)
        val status: TextView = itemView.findViewById(R.id.status)
    }
}
