package com.example.a2lytics.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.a2lytics.data.PropertyDatabase
import com.example.a2lytics.data.PropertyEntity
import com.example.a2lytics.databinding.FragmentAdvanceBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AdvanceFragment : Fragment() {

    private lateinit var binding : FragmentAdvanceBinding

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdvanceBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.putPropertyBtn.setOnClickListener {
            val propertyName = binding.propertyName.text.toString()
            val propertyFor = binding.propertyFor.text.toString()
            val numberOfRooms = binding.numberOfRooms.text.toString().toIntOrNull() ?: 0
            val bedsPerRoom = binding.bedsPerRoom.text.toString().toIntOrNull() ?: 0
            val roomRent = binding.roomRent.text.toString().toDoubleOrNull() ?: 0.0
            val contactNumber = binding.contactNumber.text.toString()
            val distanceFromDYPTC =
                binding.distanceFromCollege.text.toString().toDoubleOrNull() ?: 0.0
            val onlinePaymentNumber = binding.onlinePaymentNumber.text.toString()

            val db = PropertyDatabase.getDatabase(requireContext())
            val property = PropertyEntity(
                propertyName = propertyName,
                propertyFor = propertyFor,
                numberOfRooms = numberOfRooms,
                bedsPerRoom = bedsPerRoom,
                roomRent = roomRent,
                contactNumber = contactNumber,
                distanceFromDYPTC = distanceFromDYPTC,
                onlinePaymentNumber = onlinePaymentNumber
            )

            CoroutineScope(Dispatchers.IO).launch {
                db.propertyDao().insertProperty(property)
            }
        }
    }
}
