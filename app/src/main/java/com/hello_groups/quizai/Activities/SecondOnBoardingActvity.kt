package com.hello_groups.quizai.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hello_groups.quizai.databinding.ActivitySecondOnBoardingBinding

class SecondOnBoardingActvity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondOnBoardingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.skip2Btn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        binding.nextBtn2.setOnClickListener {
            startActivity(Intent(this, ThirdOnBoardingActivity::class.java))
        }
    }
}