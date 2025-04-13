package com.example.a2lytics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a2lytics.data.PropertyEntity

class PropertyAdapter(private val properties: List<PropertyEntity>) :
    RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder>() {

    inner class PropertyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.propertyTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_property, parent, false)
        return PropertyViewHolder(view)
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        val property = properties[position]
        holder.textView.text = """
            🏡 ${property.propertyName} (${property.propertyFor})
            🛏️ Rooms: ${property.numberOfRooms} | Beds: ${property.bedsPerRoom}
            💸 Rent: ₹${property.roomRent}
            📞 Contact: ${property.contactNumber}
            📍 ${property.distanceFromDYPTC} km from DYPTC
            🏦 Pay No: ${property.onlinePaymentNumber}
        """.trimIndent()
    }

    override fun getItemCount(): Int = properties.size
}