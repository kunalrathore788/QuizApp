package com.hello_groups.quizai.Activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.hello_groups.quizai.Models.newUser
import com.hello_groups.quizai.R
import com.hello_groups.quizai.databinding.ActivityVideoCallMainBinding


class VideoCall_Main_Activity : AppCompatActivity() {
    private lateinit var fireStore:FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var binding : ActivityVideoCallMainBinding
    var coins: Long = 0
    val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )
    private val requestCode = 1
    private lateinit var user: newUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoCallMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fireStore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser? = auth.currentUser

        fireStore.collection("users")
            .document(currentUser?.uid ?: "")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Handle error if needed
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    user = snapshot.toObject(newUser::class.java)!!
                    coins = user.getCoins()!!
                    binding.coins.text = "You Have: ${coins}"

                    if (user.getProfile() == null) {
                        Glide.with(this)
                            .load(R.drawable.demo_user)
                            .into(binding.userProfile)
                    } else {
                        Glide.with(this)
                            .load(user.getProfile())
                            .into(binding.userProfile)
                    }
                }
            }
        binding.findButton.setOnClickListener {
            if (isPermissionsGranted()) {
                if (coins > 5) {
                    coins -= 5
                    val profilesRef = fireStore.collection("users")
                    val currentUserRef = profilesRef.document(auth.uid!!)
                    currentUserRef.update("coins", coins)
                        .addOnSuccessListener {
                            val intent = Intent(this, ConnectingActivity::class.java)
                            intent.putExtra("profile", user.getProfile())
                            startActivity(intent)
                        }
                        .addOnFailureListener { exception ->
                            // Handle the failure case
                        }
                } else {
                    Toast.makeText(this, "Insufficient Coins", Toast.LENGTH_SHORT).show()
                }
            } else {
                askPermissions()
            }
        }
    }

    private fun askPermissions() {
        ActivityCompat.requestPermissions(this@VideoCall_Main_Activity, permissions, requestCode);
    }

    private fun isPermissionsGranted(): Boolean {
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(this@VideoCall_Main_Activity, permission) != PackageManager.PERMISSION_GRANTED)
                return false
        }

        return true
    }
}