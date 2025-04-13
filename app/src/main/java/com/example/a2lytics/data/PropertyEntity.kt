package com.example.a2lytics.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rental_properties")
data class PropertyEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val propertyName: String,
    val propertyFor: String,
    val numberOfRooms: Int,
    val bedsPerRoom: Int,
    val roomRent: Double,
    val contactNumber: String,
    val distanceFromDYPTC: Double,
    val onlinePaymentNumber: String
)