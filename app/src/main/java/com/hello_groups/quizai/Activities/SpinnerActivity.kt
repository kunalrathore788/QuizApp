package com.hello_groups.quizai.Activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.hello_groups.quizai.SpinWheel.model.LuckyItem
import com.hello_groups.quizai.databinding.ActivitySpinnerActivityBinding
import java.util.*
import kotlin.collections.ArrayList

class SpinnerActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySpinnerActivityBinding
    private var spinCount : Int = 1
    private var lastSpinTime: Date? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpinnerActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data: MutableList<LuckyItem> = ArrayList()

        val luckyItem1 = LuckyItem()
        luckyItem1.topText = "5"
        luckyItem1.secondaryText = "COINS"
        luckyItem1.textColor = Color.parseColor("#212121")
        luckyItem1.color = Color.parseColor("#eceff1")
        data.add(luckyItem1)

        val luckyItem2 = LuckyItem()
        luckyItem2.topText = "10"
        luckyItem2.secondaryText = "COINS"
        luckyItem2.color = Color.parseColor("#00cf00")
        luckyItem2.textColor = Color.parseColor("#ffffff")
        data.add(luckyItem2)

        val luckyItem3 = LuckyItem()
        luckyItem3.topText = "15"
        luckyItem3.secondaryText = "COINS"
        luckyItem3.textColor = Color.parseColor("#212121")
        luckyItem3.color = Color.parseColor("#eceff1")
        data.add(luckyItem3)

        val luckyItem4 = LuckyItem()
        luckyItem4.topText = "20"
        luckyItem4.secondaryText = "COINS"
        luckyItem4.color = Color.parseColor("#7f00d9")
        luckyItem4.textColor = Color.parseColor("#ffffff")
        data.add(luckyItem4)

        val luckyItem5 = LuckyItem()
        luckyItem5.topText = "25"
        luckyItem5.secondaryText = "COINS"
        luckyItem5.textColor = Color.parseColor("#212121")
        luckyItem5.color = Color.parseColor("#eceff1")
        data.add(luckyItem5)

        val luckyItem6 = LuckyItem()
        luckyItem6.topText = "30"
        luckyItem6.secondaryText = "COINS"
        luckyItem6.color = Color.parseColor("#dc0000")
        luckyItem6.textColor = Color.parseColor("#ffffff")
        data.add(luckyItem6)

        val luckyItem7 = LuckyItem()
        luckyItem7.topText = "35"
        luckyItem7.secondaryText = "COINS"
        luckyItem7.textColor = Color.parseColor("#212121")
        luckyItem7.color = Color.parseColor("#eceff1")
        data.add(luckyItem7)

        val luckyItem8 = LuckyItem()
        luckyItem8.topText = "0"
        luckyItem8.secondaryText = "COINS"
        luckyItem8.color = Color.parseColor("#008bff")
        luckyItem8.textColor = Color.parseColor("#ffffff")
        data.add(luckyItem8)

        binding.wheelview.setData(data)
        binding.wheelview.setRound(5)

        val userRef = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().uid ?: "")

        userRef.get().addOnSuccessListener { documentSnapshot ->
            lastSpinTime = documentSnapshot.getTimestamp("lastSpinTime")?.toDate()

            // Check if 12 hours have passed
            val currentTime = Calendar.getInstance().time
            val twelveHoursAgo = Calendar.getInstance()
            twelveHoursAgo.add(Calendar.HOUR_OF_DAY, -12)

            if (lastSpinTime != null && lastSpinTime!! > twelveHoursAgo.time) {
                spinCount = 0
            }

            binding.spinBtn.setOnClickListener {
                if (spinCount != 0) {
                    val randomNumber = Random().nextInt(8)
                    binding.wheelview.startLuckyWheelWithTargetIndex(randomNumber)

                    // Update lastSpinTime and spinCount in Firebase
                    val dataToUpdate = hashMapOf(
                        "lastSpinTime" to FieldValue.serverTimestamp(),
                        "spinCount" to spinCount - 1
                    )

                    userRef.update(dataToUpdate)
                        .addOnSuccessListener {
                            spinCount--
                        }
                        .addOnFailureListener { exception ->
                            // Handle the failure case
                        }
                } else {
                    Toast.makeText(this, "You have used your spin chance. Try again after 12 hours.", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.wheelview.setLuckyRoundItemSelectedListener { index ->
            updateCash(index)
        }
    }

    private fun updateCash(index: Int) {
        var cash: Long = 0
        when (index) {
            0 -> cash = 5
            1 -> cash = 10
            2 -> cash = 15
            3 -> cash = 20
            4 -> cash = 25
            5 -> cash = 30
            6 -> cash = 35
            7 -> cash = 0

        }

        val database = FirebaseFirestore.getInstance()

        database.collection("users")
            .document(FirebaseAuth.getInstance().uid!!)
            .update("coins", FieldValue.increment(cash))
            .addOnSuccessListener {
                Toast.makeText(this@SpinnerActivity, "Coins added in account.", Toast.LENGTH_SHORT).show()
                finish()
        }
    }
}