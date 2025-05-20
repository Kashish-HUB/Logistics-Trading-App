package com.example.transportproject

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VehicleMaintenanceAdapter(
    private val vehicleList: List<VehicleData>,
    private val onViewHistoryClick: (Int) -> Unit,
    private val onBookMaintenanceClick: (Int) -> Unit
) : RecyclerView.Adapter<VehicleMaintenanceAdapter.VehicleViewHolder>() {
    inner class VehicleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val vehicleImage: ImageView = itemView.findViewById(R.id.vehicleImage)
        val vehicleNumber: TextView = itemView.findViewById(R.id.vehicleNumber)
        val vehicleModel: TextView = itemView.findViewById(R.id.vehicleModel)
        val vehicleType: TextView = itemView.findViewById(R.id.vehicleType)
        val viewHistoryBtn: Button = itemView.findViewById(R.id.btnViewHistory)
        val bookMaintenanceBtn: Button = itemView.findViewById(R.id.btnBookMaintenance)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vehicle_maintenance_card, parent, false)
        return VehicleViewHolder(view)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        val vehicle = vehicleList[position]

        holder.vehicleNumber.text = "Vehicle Number: ${vehicle.vehicleNumber ?: "N/A"}"
        holder.vehicleModel.text = "Model: ${vehicle.vehicleModel ?: "N/A"}"
        holder.vehicleType.text = "Type: ${vehicle.vehicleType ?: "N/A"}"

        try {
            if (!vehicle.imageUri.isNullOrEmpty()) {
                holder.vehicleImage.setImageURI(Uri.parse(vehicle.imageUri))
            } else {
                holder.vehicleImage.setImageResource(R.drawable.default_vehicle_image)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            holder.vehicleImage.setImageResource(R.drawable.default_vehicle_image)
        }

        holder.viewHistoryBtn.setOnClickListener {
            onViewHistoryClick(position)
        }

        holder.bookMaintenanceBtn.setOnClickListener {
            onBookMaintenanceClick(position)
        }
    }

    override fun getItemCount() = vehicleList.size
}
