package com.example.a2lytics

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.a2lytics.data.PropertyDatabase
import com.example.a2lytics.data.PropertyEntity
import com.example.a2lytics.data.UserRole
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailScreen : AppCompatActivity() {

    private lateinit var tvPropertyName: TextView
    private lateinit var tvTotalBeds: TextView
    private lateinit var tvAvailableBeds: TextView
    private lateinit var tvRent: TextView
    private lateinit var tvContact: TextView
    private lateinit var tvGpay: TextView
    private lateinit var tvDistance: TextView
    private lateinit var tvAddress: TextView
    private lateinit var ivGender: ImageView
    private lateinit var editFab: FloatingActionButton
    private var currentProperty: PropertyEntity? = null

    private val updatePropertyLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            // Refresh the property details
            currentProperty?.propertyName?.let { loadPropertyDetails(it) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_screen)
        
        initializeViews()

        // Get property name from intent
        val propertyName = intent.getStringExtra("propertyName")
        val propertyFor = intent.getStringExtra("propertyFor") ?: ""
        
        if (propertyName == null) {
            Toast.makeText(this, "Error: Property name not provided", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Set gender image immediately based on propertyFor
        val imageResource = when (propertyFor.lowercase()) {
            "boy"  -> R.drawable.student
            "Boy"  -> R.drawable.student
            "boys"  -> R.drawable.student
            "Boys"  -> R.drawable.student
            "male"  -> R.drawable.student
            "males"  -> R.drawable.student
            "b"  -> R.drawable.student
            "B"  -> R.drawable.student
            "M"  -> R.drawable.student
            "m"  -> R.drawable.student
            else -> R.drawable.smile
        }
        ivGender.setImageResource(imageResource)

        // Show edit button only for property owners
        if (!UserRole.isStudent(this)) {
            editFab.visibility = View.VISIBLE
        }
        
        loadPropertyDetails(propertyName)

        // Set up edit button click listener
        editFab.setOnClickListener {
            currentProperty?.let { property ->
                val intent = Intent(this, UpdatePropertyActivity::class.java).apply {
                    putExtra("propertyId", property.id)
                    putExtra("propertyName", property.propertyName)
                    putExtra("propertyFor", property.propertyFor)
                    putExtra("numberOfRooms", property.numberOfRooms)
                    putExtra("bedsPerRoom", property.bedsPerRoom)
                    putExtra("roomRent", property.roomRent)
                    putExtra("contactNumber", property.contactNumber)
                    putExtra("distanceFromDYPTC", property.distanceFromDYPTC)
                    putExtra("onlinePaymentNumber", property.onlinePaymentNumber)
                    putExtra("ownerAddress", property.ownerAddress)
                }
                updatePropertyLauncher.launch(intent)
            }
        }
    }

    private fun loadPropertyDetails(propertyName: String) {
        val db = PropertyDatabase.getDatabase(this)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val property = db.propertyDao().getPropertyByName(propertyName)
                withContext(Dispatchers.Main) {
                    if (property != null) {
                        currentProperty = property
                        showPropertyCard(property)
                    } else {
                        Toast.makeText(this@DetailScreen, "Property not found: $propertyName", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            } catch (e: Exception) {
                Log.e("DetailScreen", "Error loading property: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DetailScreen, "Error loading property details", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun initializeViews() {
        try {
            tvPropertyName = findViewById(R.id.tvPropertyName)
            tvTotalBeds = findViewById(R.id.tvTotalBeds)
            tvAvailableBeds = findViewById(R.id.tvAvailableBeds)
            tvRent = findViewById(R.id.tvRent)
            tvContact = findViewById(R.id.tvContact)
            tvGpay = findViewById(R.id.tvGpay)
            tvDistance = findViewById(R.id.tvDistance)
            tvAddress = findViewById(R.id.tvAddress)
            ivGender = findViewById(R.id.ivGender)
            editFab = findViewById(R.id.editFab)
        } catch (e: Exception) {
            Log.e("DetailScreen", "Error initializing views: ${e.message}", e)
            Toast.makeText(this, "Error initializing views", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun showPropertyCard(property: PropertyEntity) {
        try {
            val totalBeds = property.numberOfRooms * property.bedsPerRoom
            
            tvTotalBeds.text = totalBeds.toString()
            tvAvailableBeds.text = "${property.bedsPerRoom}"
            tvRent.text = "₹${property.roomRent}/month"
            tvContact.text = property.contactNumber
            tvPropertyName.text = property.propertyName
            tvGpay.text = property.onlinePaymentNumber
            tvDistance.text = "${property.distanceFromDYPTC} km"
            tvAddress.text = property.ownerAddress
            
            Log.d("DetailScreen", "Property details updated successfully")
        } catch (e: Exception) {
            Log.e("DetailScreen", "Error updating property details: ${e.message}", e)
            Toast.makeText(this, "Error updating property details", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}