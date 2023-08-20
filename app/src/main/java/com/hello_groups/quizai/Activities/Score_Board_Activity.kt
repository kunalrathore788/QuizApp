package com.hello_groups.quizai.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hello_groups.quizai.R
import com.hello_groups.quizai.databinding.ActivityScoreBoardBinding

class Score_Board_Activity : AppCompatActivity() {
    private lateinit var binding: ActivityScoreBoardBinding
    private val POINTS = 10
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoreBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val correctAnswers = intent.getIntExtra("correct", 0)
        val totalQuestions = intent.getIntExtra("total", 0)

        val points = correctAnswers * POINTS.toLong()

        binding.score.text = String.format("%d/%d", correctAnswers, totalQuestions)
        binding.correctQuestions.text = String.format("%d",correctAnswers)
        binding.wrongQuestions.text = String.format("%d",totalQuestions-correctAnswers)
        binding.totalQuestions.text = String.format("%d",totalQuestions)
//        binding.earnedCoins.text = points.toString()

        val database = FirebaseFirestore.getInstance()

        val userId = FirebaseAuth.getInstance().currentUser?.uid

//        if (userId != null) {
//            database.collection("users")
//                .document(userId)
//                .update("coins", FieldValue.increment(points))
//        }

        binding.restartBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }
    }
}
