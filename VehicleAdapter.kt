package com.example.transportproject

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VehicleAdapter(
    private val vehicleList: List<VehicleData>,
    private val onViewHistoryClick: (Int) -> Unit,
    private val onBookMaintenanceClick: (Int) -> Unit
) : RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder>() {

    inner class VehicleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val vehicleImage: ImageView = itemView.findViewById(R.id.vehicleImage)
        val driverName: TextView = itemView.findViewById(R.id.driverName)
        val driverContact: TextView = itemView.findViewById(R.id.driverContact)
        val vehicleNumber: TextView = itemView.findViewById(R.id.vehicleNumber)
        val vehicleModel: TextView = itemView.findViewById(R.id.vehicleModel)
        val vehicleType: TextView = itemView.findViewById(R.id.vehicleType)
        val loadCapacity: TextView = itemView.findViewById(R.id.loadCapacity)
        val viewHistoryButton: Button = itemView.findViewById(R.id.btnViewHistory)
        val bookMaintenanceButton: Button = itemView.findViewById(R.id.btnBookMaintenance)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vehicle_card, parent, false)
        return VehicleViewHolder(view)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        val vehicle = vehicleList[position]

        holder.driverName.text = "Driver Name: ${vehicle.driverName}"
        holder.driverContact.text = "Contact: ${vehicle.driverContact}"
        holder.vehicleNumber.text = "Vehicle Number: ${vehicle.vehicleNumber}"
        holder.vehicleModel.text = "Model: ${vehicle.vehicleModel}"
        holder.vehicleType.text = "Type: ${vehicle.vehicleType}"
        holder.loadCapacity.text = "Capacity: ${vehicle.loadCapacity}"

        if (!vehicle.imageUri.isNullOrEmpty()) {
            holder.vehicleImage.setImageURI(Uri.parse(vehicle.imageUri))
        }

        holder.viewHistoryButton.setOnClickListener { onViewHistoryClick(position) }
        holder.bookMaintenanceButton.setOnClickListener { onBookMaintenanceClick(position) }
    }

    override fun getItemCount() = vehicleList.size
}
