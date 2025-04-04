package com.example.a2lytics

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.a2lytics.databinding.ActivityMainBinding
import com.example.a2lytics.drawer.AboutUs
import com.example.a2lytics.drawer.Edit_Profile
import com.example.a2lytics.drawer.Help
import com.example.a2lytics.drawer.Settings
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {



    private lateinit var toggle : ActionBarDrawerToggle

    private lateinit var  binding : ActivityMainBinding


    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val drawerLayout : DrawerLayout = binding.main

        // Set up NavController with NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        // Set up SmoothBottomBar with NavController
        binding.smoothBottomBar.setupWithNavController(navController)

        // Set up notification icon click listener
        binding.notificationIcon.setOnClickListener {
                val intent = Intent(this, NotificationsAndMessages::class.java)
                startActivity(intent)
            }


        // Set up navigation drawer
        val humburgerButton = binding.humburgerButton
        val navView : NavigationView = binding.navView

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        humburgerButton.setOnClickListener {
            drawerLayout.openDrawer(navView)
        }

        val editProfile = R.id.editProfile
        val help = R.id.help
        val aboutUs = R.id.aboutUs
        val settings = R.id.settings
        val logOut = R.id.logOut

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                editProfile -> {
                    startActivity(Intent(this, Edit_Profile::class.java))
                }
                help -> {
                    startActivity(Intent(this, Help::class.java))
                }
                aboutUs -> {
                    startActivity(Intent(this, AboutUs::class.java))
                }
                settings -> {
                    startActivity(Intent(this, Settings::class.java))
                }
                logOut -> {
                    FirebaseAuth.getInstance().signOut()

                    startActivity(Intent(this, Log_In::class.java))
                    finish()
                }
            }
            true
        }
    }

   @Deprecated("Deprecated in Java")
    override fun onBackPressed() { // Handle back button press for drawer layout
        val drawerLayout : DrawerLayout = binding.main

        if (drawerLayout.isDrawerOpen(binding.navView)) {
            drawerLayout.closeDrawer(binding.navView)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { // Handle menu item selection

        if(toggle.onOptionsItemSelected(item)){ // Handle toggle item selection
            return true
        }

        return super.onOptionsItemSelected(item) // Handle other item selections


    }



}
