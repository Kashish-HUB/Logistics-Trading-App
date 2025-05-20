package com.example.transportproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class SupportTicket(val description: String, val imageUrl: String?)

class SupportTicketAdapter(private val context: Context, private val ticketList: List<SupportTicket>) :
    RecyclerView.Adapter<SupportTicketAdapter.TicketViewHolder>() {

    class TicketViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val issueDescription: TextView = view.findViewById(R.id.issueDescriptionText)
        val issueImage: ImageView = view.findViewById(R.id.issueImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_support_ticket, parent, false)
        return TicketViewHolder(view)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val ticket = ticketList[position]
        holder.issueDescription.text = ticket.description

        if (ticket.imageUrl != null) {
            holder.issueImage.visibility = View.VISIBLE
            holder.issueImage.setImageURI(android.net.Uri.parse(ticket.imageUrl))
        } else {
            holder.issueImage.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = ticketList.size
}
