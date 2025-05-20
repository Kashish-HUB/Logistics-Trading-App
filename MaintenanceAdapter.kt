package com.example.transportproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class MaintenanceAdapter : ListAdapter<String, MaintenanceAdapter.MaintenanceViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
            override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaintenanceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return MaintenanceViewHolder(view)
    }

    override fun onBindViewHolder(holder: MaintenanceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MaintenanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(detail: String) {
            (itemView as TextView).text = detail
        }
    }
}
