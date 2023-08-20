package com.hello_groups.quizai.Activities


import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.hello_groups.quizai.Models.newUser
import com.hello_groups.quizai.R
import com.hello_groups.quizai.databinding.ActivityRegisterBinding


class RegisterActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var signInLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.signInText.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Configure Google Sign-In options
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        // Create a GoogleSignInClient object
        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.googleBtn.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            signInLauncher.launch(signInIntent)
        }
        // Create the launcher for starting the sign-in intent
        signInLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                val resultCode = result.resultCode
                val data = result.data

                if (resultCode == RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    try {
                        // Google Sign-In was successful, authenticate with Firebase
                        val account = task.getResult(ApiException::class.java)
                        firebaseAuthWithGoogle(account)
                    } catch (e: ApiException) {
                        // Google Sign-In failed, handle the error
                        Log.e(TAG, "Google sign-in failed", e)
                        Toast.makeText(this, "Google sign-in failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        // Start the Google Sign-In intent
        val signInIntent = googleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)

        binding.signBtn.setOnClickListener {
            val email = binding.emailBox.text.toString()
            val pass = binding.passwordBox.text.toString()
            val name = binding.nameBox.text.toString()
            val referCode = binding.referralBox.text.toString()

            val user = newUser(name, email, pass, referCode, profile = null, 0L)

            firebaseAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val uid = task.result?.user?.uid
                        uid?.let {
                            firestore.collection("users").document(it)
                                .set(user)
                                .addOnCompleteListener { innerTask ->
                                    if (innerTask.isSuccessful) {
                                        startActivity(Intent(this, MainActivity::class.java))
                                        finish()
                                    } else {
                                        Toast.makeText(this, innerTask.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }
                    } else {
                        Toast.makeText(this, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    saveUserDataToFirestore(user, account)
                } else {
                    Log.e(TAG, "sign-in failed", task.exception)
                    Toast.makeText(this, "sign-in failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUserDataToFirestore(user: FirebaseUser?, account: GoogleSignInAccount?) {
        if (user != null && account != null) {
            val defUser: FirebaseUser? = firebaseAuth.currentUser

            // Fetch existing user data from Firestore
            firestore.collection("users").document(defUser?.uid ?: "")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document: DocumentSnapshot? = task.result
                        if (document != null && document.exists()) {
                            val coins = document.getLong("coins") ?: 0L

                            // Update existing user's coin value
                            firestore.collection("users").document(defUser?.uid ?: "")
                                .update("coins", coins)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "User coins updated", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, MainActivity::class.java))
                                }
                                .addOnFailureListener {
                                }
                        } else {
                            // Create new user data with initial coins value
                            val firebaseUser = newUser(defUser?.displayName.toString(), defUser?.email.toString(), "", "1234",
                                defUser?.photoUrl.toString(),
                                0L
                            )

                            // Save user data to Firestore
                            firestore.collection("users").document(user.uid)
                                .set(firebaseUser)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "User data saved to Firestore", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, MainActivity::class.java))
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Error saving user data to Firestore", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        Toast.makeText(this, "Error fetching user data", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}