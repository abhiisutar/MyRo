package com.example.a2lytics.fragment

import android.annotation.TargetApi
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.a2lytics.R
import com.example.a2lytics.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase Auth and Database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        // Get Current User ID
        val currentUserId = auth.currentUser?.uid

        // Check if user is an owner
//        currentUserId?.let { uid ->
//            database.child("users").child(uid).child("role").addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    val userRole = snapshot.getValue(String::class.java)
//                    if (userRole == "owner") {
//                        binding.putPropertyBtn.visibility = View.VISIBLE
//                    } else {
//                        binding.putPropertyBtn.visibility = View.GONE
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    binding.putPropertyBtn.visibility = View.GONE
//                }
//            })
//        }
//
//        binding.searchRoomBtn.setOnClickListener {
//            findNavController().navigate(R.id.action_homeFragment_to_basicFragment)
//        }
//
//        binding.putPropertyBtn.setOnClickListener {
//            findNavController().navigate(R.id.action_homeFragment_to_AdvanceFragment)
//        }
     }
}
