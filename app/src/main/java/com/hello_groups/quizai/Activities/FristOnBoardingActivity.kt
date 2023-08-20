package com.hello_groups.quizai.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hello_groups.quizai.databinding.ActivityFristOnBoardingBinding

class FristOnBoardingActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding:ActivityFristOnBoardingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFristOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.skip1Btn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
        binding.nextBtn1.setOnClickListener {
            startActivity(Intent(this, SecondOnBoardingActvity::class.java))
        }
    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
}