package com.example.a2lytics.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete

@Dao
interface PropertyDao {
    @Insert
    suspend fun insertProperty(property: PropertyEntity)

    @Update
    suspend fun updateProperty(property: PropertyEntity)

    @Delete
    suspend fun deleteProperty(property: PropertyEntity)

    @Query("SELECT * FROM rental_properties")
    suspend fun getAllProperties(): List<PropertyEntity>

    @Query("SELECT * FROM rental_properties WHERE propertyName = :name LIMIT 1")
    suspend fun getPropertyByName(name: String): PropertyEntity?
}