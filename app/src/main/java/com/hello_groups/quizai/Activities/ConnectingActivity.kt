package com.hello_groups.quizai.Activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.hello_groups.quizai.databinding.ActivityConnectingBinding


class ConnectingActivity : AppCompatActivity() {
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var auth : FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var binding: ActivityConnectingBinding
    var isOkay = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConnectingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fireStore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val profile = intent.getStringExtra("profile")
        Glide.with(this)
            .load(profile)
            .into(binding.userProfile)

        val username = auth.uid

        database.reference.child("users")
            .orderByChild("status")
            .equalTo(0.0).limitToFirst(1)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.childrenCount > 0) {
                        isOkay = true
                        // Room Available
                        for (childSnap in snapshot.children) {
                            database.reference
                                .child("users")
                                .child(childSnap.key!!)
                                .child("incoming")
                                .setValue(username)
                            database.reference
                                .child("users")
                                .child(childSnap.key!!)
                                .child("status")
                                .setValue(1)
                            val intent = Intent(this@ConnectingActivity, CallActivity::class.java)
                            val incoming = childSnap.child("incoming").getValue(
                                String::class.java
                            )
                            val createdBy = childSnap.child("createdBy").getValue(
                                String::class.java
                            )
                            val isAvailable = childSnap.child("isAvailable").getValue(
                                Boolean::class.java
                            )!!
                            intent.putExtra("username", username)
                            intent.putExtra("incoming", incoming)
                            intent.putExtra("createdBy", createdBy)
                            intent.putExtra("isAvailable", isAvailable)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        // Not Available
                        val room: HashMap<String, Any> = HashMap()
                        room["incoming"] = username!!
                        room["createdBy"] = username!!
                        room["isAvailable"] = true
                        room["status"] = 0
                        database.reference
                            .child("users")
                            .child(username!!)
                            .setValue(room).addOnSuccessListener {
                                database.reference
                                    .child("users")
                                    .child(username!!)
                                    .addValueEventListener(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if (snapshot.child("status").exists()) {
                                                if (snapshot.child("status")
                                                        .getValue(Int::class.java) == 1
                                                ) {
                                                    if (isOkay) return
                                                    isOkay = true
                                                    val intent = Intent(
                                                        this@ConnectingActivity,
                                                        CallActivity::class.java
                                                    )
                                                    val incoming =
                                                        snapshot.child("incoming").getValue(
                                                            String::class.java
                                                        )
                                                    val createdBy =
                                                        snapshot.child("createdBy").getValue(
                                                            String::class.java
                                                        )
                                                    val isAvailable =
                                                        snapshot.child("isAvailable").getValue(
                                                            Boolean::class.java
                                                        )!!
                                                    intent.putExtra("username", username)
                                                    intent.putExtra("incoming", incoming)
                                                    intent.putExtra("createdBy", createdBy)
                                                    intent.putExtra("isAvailable", isAvailable)
                                                    startActivity(intent)
                                                    finish()
                                                }
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {}
                                    })
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }
}