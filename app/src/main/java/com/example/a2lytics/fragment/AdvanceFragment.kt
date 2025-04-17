package com.example.a2lytics.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.a2lytics.R
import com.example.a2lytics.data.PropertyDatabase
import com.example.a2lytics.data.PropertyEntity
import com.example.a2lytics.databinding.FragmentAdvanceBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdvanceFragment : Fragment() {

    private lateinit var binding: FragmentAdvanceBinding
    private var isEditing = false
    private var propertyId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdvanceBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check if we're in edit mode
        activity?.intent?.let { intent ->
            isEditing = intent.getBooleanExtra("isEditing", false)
            if (isEditing) {
                propertyId = intent.getIntExtra("propertyId", 0)
                // Fill the form with existing data
                binding.propertyName.setText(intent.getStringExtra("propertyName"))
                binding.propertyFor.setText(intent.getStringExtra("propertyFor"))
                binding.numberOfRooms.setText(intent.getIntExtra("numberOfRooms", 0).toString())
                binding.bedsPerRoom.setText(intent.getIntExtra("bedsPerRoom", 0).toString())
                binding.roomRent.setText(intent.getDoubleExtra("roomRent", 0.0).toString())
                binding.contactNumber.setText(intent.getStringExtra("contactNumber"))
                binding.distanceFromCollege.setText(intent.getDoubleExtra("distanceFromDYPTC", 0.0).toString())
                binding.onlinePaymentNumber.setText(intent.getStringExtra("onlinePaymentNumber"))
                binding.ownerAddress.setText(intent.getStringExtra("ownerAddress"))

                // Change button text for edit mode
                binding.putPropertyBtn.text = "Update Property"
            }
        }

        binding.putPropertyBtn.setOnClickListener {
            val propertyName = binding.propertyName.text.toString()
            val propertyFor = binding.propertyFor.text.toString()
            val numberOfRoomsText = binding.numberOfRooms.text.toString()
            val bedsPerRoomText = binding.bedsPerRoom.text.toString()
            val roomRentText = binding.roomRent.text.toString()
            val contactNumber = binding.contactNumber.text.toString()
            val distanceFromCollegeText = binding.distanceFromCollege.text.toString()
            val onlinePaymentNumber = binding.onlinePaymentNumber.text.toString()
            val ownerAddress = binding.ownerAddress.text.toString()

            // Check if any field is empty
            if (propertyName.isBlank() || propertyFor.isBlank() || numberOfRoomsText.isBlank()
                || bedsPerRoomText.isBlank() || roomRentText.isBlank() || contactNumber.isBlank()
                || distanceFromCollegeText.isBlank() || onlinePaymentNumber.isBlank()
                || ownerAddress.isBlank()
            ) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val numberOfRooms = numberOfRoomsText.toIntOrNull() ?: 0
            val bedsPerRoom = bedsPerRoomText.toIntOrNull() ?: 0
            val roomRent = roomRentText.toDoubleOrNull() ?: 0.0
            val distanceFromDYPTC = distanceFromCollegeText.toDoubleOrNull() ?: 0.0

            val db = PropertyDatabase.getDatabase(requireContext())
            val property = PropertyEntity(
                id = if (isEditing) propertyId else 0,
                propertyName = propertyName,
                propertyFor = propertyFor,
                numberOfRooms = numberOfRooms,
                bedsPerRoom = bedsPerRoom,
                roomRent = roomRent,
                contactNumber = contactNumber,
                distanceFromDYPTC = distanceFromDYPTC,
                onlinePaymentNumber = onlinePaymentNumber,
                ownerAddress = ownerAddress
            )

            CoroutineScope(Dispatchers.IO).launch {
                if (isEditing) {
                    db.propertyDao().updateProperty(property)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Property updated successfully", Toast.LENGTH_SHORT).show()
                        activity?.finish()
                    }
                } else {
                    db.propertyDao().insertProperty(property)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Property saved successfully", Toast.LENGTH_SHORT).show()
                        clearForm()
                    }
                }
            }
        }
    }

    private fun clearForm() {
        binding.propertyName.text?.clear()
        binding.propertyFor.text?.clear()
        binding.numberOfRooms.text?.clear()
        binding.bedsPerRoom.text?.clear()
        binding.roomRent.text?.clear()
        binding.contactNumber.text?.clear()
        binding.distanceFromCollege.text?.clear()
        binding.onlinePaymentNumber.text?.clear()
        binding.ownerAddress.text?.clear()
    }
}
