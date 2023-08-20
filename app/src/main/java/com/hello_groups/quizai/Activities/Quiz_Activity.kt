package com.hello_groups.quizai.Activities

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.hello_groups.quizai.Models.Questions
import com.hello_groups.quizai.R
import com.hello_groups.quizai.databinding.ActivityQuizBinding
import java.util.*
import kotlin.collections.ArrayList

class Quiz_Activity : AppCompatActivity() {

    private var questions: ArrayList<Questions> = ArrayList()
    private var index = 0
    private lateinit var question: Questions
    private lateinit var timer: CountDownTimer
    private lateinit var database: FirebaseFirestore
    private var correctAnswers = 0

    private lateinit var binding: ActivityQuizBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }

        database = FirebaseFirestore.getInstance()
        val catId = intent.getStringExtra("catId")

        val random = Random()
        val rand = random.nextInt(2)

        database.collection("categories")
            .document(catId!!)
            .collection("questions")
            .whereGreaterThanOrEqualTo("index", rand)
            .orderBy("index")
            .limit(2).get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                if (queryDocumentSnapshots.documents.size < 2) {
                    database.collection("categories")
                        .document(catId)
                        .collection("questions")
                        .whereLessThanOrEqualTo("index", rand)
                        .orderBy("index")
                        .limit(2).get()
                        .addOnSuccessListener { queryDocumentSnapshots ->
                            for (snapshot in queryDocumentSnapshots) {
                                val question = snapshot.toObject(Questions::class.java)
                                questions.add(question)
                            }
                            setNextQuestion()
                        }
                } else {
                    for (snapshot in queryDocumentSnapshots) {
                        val question = snapshot.toObject(Questions::class.java)
                        questions.add(question)
                    }
                    setNextQuestion()
                }
            }

        resetTimer()
    }

    private fun showQuizEndDialog() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Quiz Ending")
        alertDialog.setMessage("The quiz will be ending soon. Are you sure you want to continue?")
        alertDialog.setPositiveButton("Continue") { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
            val intent = Intent(this@Quiz_Activity, Score_Board_Activity::class.java)
            intent.putExtra("correct", correctAnswers)
            intent.putExtra("total", questions.size)
            startActivity(intent)
            finish()
        }
        alertDialog.setNegativeButton("Cancel") { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
            // Handle cancellation or any other logic
        }
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun resetTimer() {
        timer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.timer.text = (millisUntilFinished / 1000).toString()
            }
            override fun onFinish() {
                Toast.makeText(this@Quiz_Activity,"TimeUp!",Toast.LENGTH_SHORT).show()
                val intent = Intent(this@Quiz_Activity, Score_Board_Activity::class.java)
                intent.putExtra("correct", correctAnswers)
                intent.putExtra("total", questions.size)
                startActivity(intent)
                finish()
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun showAnswer() {
        when {
            question.getAnswers() == binding.option1.text.toString() ->
                binding.option1.background = resources.getDrawable(R.drawable.option_right)
            question.getAnswers() == binding.option2.text.toString() ->
                binding.option2.background = resources.getDrawable(R.drawable.option_right)
            question.getAnswers() == binding.option3.text.toString() ->
                binding.option3.background = resources.getDrawable(R.drawable.option_right)
            question.getAnswers() == binding.option4.text.toString() ->
                binding.option4.background = resources.getDrawable(R.drawable.option_right)
        }
    }

    private fun setNextQuestion() {
        timer.cancel()
        timer.start()
        if (index < questions.size) {
            binding.questionCounter.text = String.format("%d/%d", (index + 1), questions.size)
            question = questions[index]
            binding.question.text = question.getQuestions()
            binding.option1.text = question.getOption1()
            binding.option2.text = question.getOption2()
            binding.option3.text = question.getOption3()
            binding.option4.text = question.getOption4()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun checkAnswer(textView: TextView) {
        val selectedAnswer = textView.text.toString()
        if (selectedAnswer == question.getAnswers()) {
            correctAnswers++
            textView.background = resources.getDrawable(R.drawable.option_right)
        } else {
            showAnswer()
            textView.background = resources.getDrawable(R.drawable.option_wrong)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun reset() {
        binding.option1.background = resources.getDrawable(R.drawable.option_unselected)
        binding.option2.background = resources.getDrawable(R.drawable.option_unselected)
        binding.option3.background = resources.getDrawable(R.drawable.option_unselected)
        binding.option4.background = resources.getDrawable(R.drawable.option_unselected)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.option_1, R.id.option_2, R.id.option_3, R.id.option_4 -> {
                timer.cancel()
                val selected = view as TextView
                checkAnswer(selected)
            }
            R.id.Next_Btn -> {
                if (index < questions.size-1) {
                    index++
                    setNextQuestion()
                    reset()
                } else {
                    showQuizEndDialog()
                }
            }
        }
    }

}