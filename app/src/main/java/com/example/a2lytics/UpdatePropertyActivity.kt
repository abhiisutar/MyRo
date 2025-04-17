package com.example.a2lytics

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a2lytics.data.PropertyDatabase
import com.example.a2lytics.data.PropertyEntity
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpdatePropertyActivity : AppCompatActivity() {
    private lateinit var propertyNameEdit: TextInputEditText
    private lateinit var propertyForEdit: TextInputEditText
    private lateinit var numberOfRoomsEdit: TextInputEditText
    private lateinit var bedsPerRoomEdit: TextInputEditText
    private lateinit var roomRentEdit: TextInputEditText
    private lateinit var contactNumberEdit: TextInputEditText
    private lateinit var distanceFromCollegeEdit: TextInputEditText
    private lateinit var onlinePaymentNumberEdit: TextInputEditText
    private lateinit var ownerAddressEdit: TextInputEditText
    private var propertyId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_property)

        initializeViews()
        loadPropertyData()
        setupUpdateButton()
    }

    private fun initializeViews() {
        propertyNameEdit = findViewById(R.id.propertyNameEdit)
        propertyForEdit = findViewById(R.id.propertyForEdit)
        numberOfRoomsEdit = findViewById(R.id.numberOfRoomsEdit)
        bedsPerRoomEdit = findViewById(R.id.bedsPerRoomEdit)
        roomRentEdit = findViewById(R.id.roomRentEdit)
        contactNumberEdit = findViewById(R.id.contactNumberEdit)
        distanceFromCollegeEdit = findViewById(R.id.distanceFromCollegeEdit)
        onlinePaymentNumberEdit = findViewById(R.id.onlinePaymentNumberEdit)
        ownerAddressEdit = findViewById(R.id.ownerAddressEdit)
    }

    private fun loadPropertyData() {
        intent.extras?.let { bundle ->
            propertyId = bundle.getInt("propertyId")
            propertyNameEdit.setText(bundle.getString("propertyName"))
            propertyForEdit.setText(bundle.getString("propertyFor"))
            numberOfRoomsEdit.setText(bundle.getInt("numberOfRooms").toString())
            bedsPerRoomEdit.setText(bundle.getInt("bedsPerRoom").toString())
            roomRentEdit.setText(bundle.getDouble("roomRent").toString())
            contactNumberEdit.setText(bundle.getString("contactNumber"))
            distanceFromCollegeEdit.setText(bundle.getDouble("distanceFromDYPTC").toString())
            onlinePaymentNumberEdit.setText(bundle.getString("onlinePaymentNumber"))
            ownerAddressEdit.setText(bundle.getString("ownerAddress"))
        }
    }

    private fun setupUpdateButton() {
        findViewById<android.widget.Button>(R.id.updatePropertyBtn).setOnClickListener {
            if (validateInputs()) {
                updateProperty()
            }
        }
    }

    private fun validateInputs(): Boolean {
        val fields = listOf(
            propertyNameEdit,
            propertyForEdit,
            numberOfRoomsEdit,
            bedsPerRoomEdit,
            roomRentEdit,
            contactNumberEdit,
            distanceFromCollegeEdit,
            onlinePaymentNumberEdit,
            ownerAddressEdit
        )

        for (field in fields) {
            if (field.text.isNullOrBlank()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }

    private fun updateProperty() {
        val property = PropertyEntity(
            id = propertyId,
            propertyName = propertyNameEdit.text.toString(),
            propertyFor = propertyForEdit.text.toString(),
            numberOfRooms = numberOfRoomsEdit.text.toString().toInt(),
            bedsPerRoom = bedsPerRoomEdit.text.toString().toInt(),
            roomRent = roomRentEdit.text.toString().toDouble(),
            contactNumber = contactNumberEdit.text.toString(),
            distanceFromDYPTC = distanceFromCollegeEdit.text.toString().toDouble(),
            onlinePaymentNumber = onlinePaymentNumberEdit.text.toString(),
            ownerAddress = ownerAddressEdit.text.toString()
        )

        val db = PropertyDatabase.getDatabase(this)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                db.propertyDao().updateProperty(property)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@UpdatePropertyActivity, "Property updated successfully", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@UpdatePropertyActivity, "Error updating property", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
