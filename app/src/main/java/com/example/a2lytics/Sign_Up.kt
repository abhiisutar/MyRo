package com.example.a2lytics

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.a2lytics.databinding.ActivitySignUpBinding
import com.example.a2lytics.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Sign_Up : AppCompatActivity() {

    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    private lateinit var email: String
    private lateinit var password: String
    private lateinit var userName: String


    private lateinit var auth: FirebaseAuth
    private lateinit var dataBase: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Firebase Auth and Database
        auth = FirebaseAuth.getInstance()
        dataBase = Firebase.database.reference

        // Configure Google Sign-In
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        // Set up button click listeners
        val createAccountButton = binding.createAccountBtn
        val signInButton = binding.signINBtn
        val googleSignUpButton = binding.googleBtn

        createAccountButton.setOnClickListener {
            // Get text from input fields
            email = binding.emailText.text.toString().trim()
            password = binding.passwordText.text.toString().trim()
            userName = binding.nameText.text.toString().trim()


            // Validate input fields
            if (email.isBlank() || userName.isBlank() || password.isBlank() ) {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            } else {
                createAccount(email, password)
            }
        }

        signInButton.setOnClickListener {
            startActivity(Intent(this@Sign_Up, Log_In::class.java))
        }

        googleSignUpButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            googleLauncher.launch(signInIntent)
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
                saveUserData()
                startActivity(Intent(this@Sign_Up, Log_In::class.java))
                finish()
            } else {
                Toast.makeText(this, "Account Creation Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                Log.d("Account", "createAccount: Failure - ${task.exception}")
            }
        }
    }

    private fun saveUserData() {
        val user = UserModel(
            email = email,
            password = password,
            userName = userName,

        )
        val userId = auth.currentUser!!.uid
        dataBase.child("users").child(userId).setValue(user)
    }

    // Google Sign-In Launcher
    private val googleLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account: GoogleSignInAccount = task.getResult(ApiException::class.java)!!
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                    auth.signInWithCredential(credential).addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            saveUserDataFromGoogle(auth.currentUser!!)
                            Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Google Sign-In Failed: ${authTask.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: ApiException) {
                    Toast.makeText(this, "Google Sign-In Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun saveUserDataFromGoogle(user: FirebaseUser) {
        val userModel = UserModel(
            email = user.email ?: "",
            userName = user.displayName ?: "",

        )
        val userId = user.uid
        dataBase.child("users").child(userId).setValue(userModel)
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
