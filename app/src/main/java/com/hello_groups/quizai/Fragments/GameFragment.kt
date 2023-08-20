package com.hello_groups.quizai.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hello_groups.quizai.Activities.Game_WebView_Activity
import com.hello_groups.quizai.Activities.MainActivity
import com.hello_groups.quizai.R
import com.hello_groups.quizai.databinding.FragmentGameBinding
import com.hello_groups.quizai.databinding.FragmentLeaderBoardBinding

class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            backIntent()
        }
        binding.gameBtn1.setOnClickListener {
            openURL1()
        }
        binding.gameBtn2.setOnClickListener {
            openURL2()
        }
        binding.gameBtn3.setOnClickListener {
            openURL3()
        }
        binding.gameBtn4.setOnClickListener {
            openURL4()
        }
    }

    private fun backIntent() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
    }

    private fun openURL1() {
        val intent = Intent(requireContext(), Game_WebView_Activity::class.java)
        intent.putExtra("url", "https://html5games.com/")
        startActivity(intent)
    }
    private fun openURL2() {
        val intent = Intent(requireContext(), Game_WebView_Activity::class.java)
        intent.putExtra("url", "https://poki.com/en/html5")
        startActivity(intent)
    }
    private fun openURL3() {
        val intent = Intent(requireContext(), Game_WebView_Activity::class.java)
        intent.putExtra("url", "https://www.agame.com/")
        startActivity(intent)
    }
    private fun openURL4() {
        val intent = Intent(requireContext(), Game_WebView_Activity::class.java)
        intent.putExtra("url", "https://www.mygameply.com/")
        startActivity(intent)
    }

    companion object {
    }
}