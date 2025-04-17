package com.example.a2lytics

import android.content.Intent
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
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

        val title = "🏠 ${property.propertyName} (${property.propertyFor})\n"
        val details = "🛏️ Rooms: ${property.numberOfRooms} | Beds: ${property.bedsPerRoom}\n" +
                "💸 Rent: ₹${property.roomRent}"

        val spannable = SpannableStringBuilder().apply {
            append(title)
            setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                title.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                RelativeSizeSpan(1.5f), // Increase text size by 50%
                0,
                title.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            append(details)
        }

        holder.textView.text = spannable

        // Set click listener
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailScreen::class.java).apply {
                putExtra("propertyName", property.propertyName)
                putExtra("propertyFor", property.propertyFor)
                putExtra("numberOfRooms", property.numberOfRooms)
                putExtra("bedsPerRoom", property.bedsPerRoom)
                putExtra("roomRent", property.roomRent)
                putExtra("contactNumber", property.contactNumber)
                putExtra("distanceFromDYPTC", property.distanceFromDYPTC)
                putExtra("onlinePaymentNumber", property.onlinePaymentNumber)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = properties.size
}
