package com.hello_groups.quizai.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.hello_groups.quizai.Models.WithdrawRequest
import com.hello_groups.quizai.Models.newUser
import com.hello_groups.quizai.R
import com.hello_groups.quizai.databinding.ActivityWalletBinding

class WalletActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWalletBinding
    private lateinit var database: FirebaseFirestore
    private lateinit var user: newUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseFirestore.getInstance()

        binding.backBtn.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }

        database.collection("users")
            .document(FirebaseAuth.getInstance().uid!!)
            .get()
            .addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
                user = documentSnapshot.toObject(newUser::class.java)!!
                binding.currentcoins.text = user.getCoins().toString()
            }

        binding.coin50Btn.setOnClickListener {
            val phoneNumber = binding.phoneBox.text.toString()
            if (phoneNumber.length < 10) {
                Toast.makeText(this@WalletActivity, "Phone number must be at least 10 digits long.", Toast.LENGTH_SHORT).show()
            } else {
                val coins = user.getCoins() ?: 0
                if (coins >= 50000) {
                    val uid = FirebaseAuth.getInstance().uid
                    val payPal = phoneNumber
                    val request = WithdrawRequest(uid!!, payPal, user.getName())
                    database.collection("withdraws")
                        .document(uid)
                        .set(request)
                        .addOnSuccessListener {
                            // Deduct coins by 50000
                            user.setCoins(coins - 50000)
                            database.collection("users")
                                .document(uid)
                                .set(user)
                                .addOnSuccessListener {
                                    binding.currentcoins.text = user.getCoins().toString()
                                    Toast.makeText(this@WalletActivity, "Request sent successfully.", Toast.LENGTH_SHORT).show()
                                }
                        }
                } else {
                    Toast.makeText(this@WalletActivity, "You need more coins to withdraw.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
