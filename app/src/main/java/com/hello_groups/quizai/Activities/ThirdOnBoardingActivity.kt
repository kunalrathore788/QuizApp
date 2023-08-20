package com.hello_groups.quizai.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hello_groups.quizai.databinding.ActivityThirdOnBoardingBinding

class ThirdOnBoardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityThirdOnBoardingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.skip3Btn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
        binding.nextBtn3.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}