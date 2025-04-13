package com.example.a2lytics.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PropertyDao {
    @Insert
    suspend fun insertProperty(property: PropertyEntity)

    @Query("SELECT * FROM rental_properties")
    suspend fun getAllProperties(): List<PropertyEntity>
}