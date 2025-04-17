package com.example.a2lytics.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [PropertyEntity::class], version = 2)
abstract class PropertyDatabase : RoomDatabase() {
    abstract fun propertyDao(): PropertyDao

    companion object {
        @Volatile
        private var INSTANCE: PropertyDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE rental_properties ADD COLUMN ownerAddress TEXT NOT NULL DEFAULT ''")
            }
        }

        fun getDatabase(context: Context): PropertyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PropertyDatabase::class.java,
                    "property_database"
                )
                .addMigrations(MIGRATION_1_2)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}