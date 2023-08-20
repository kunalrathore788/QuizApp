package com.hello_groups.quizai.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.hello_groups.quizai.Activities.MainActivity
import com.hello_groups.quizai.Adapters.LeaderBoard_Adapter
import com.hello_groups.quizai.Models.newUser
import com.hello_groups.quizai.R
import com.hello_groups.quizai.databinding.FragmentLeaderBoardBinding

class LeaderBoardFragment : Fragment() {

    private lateinit var rvAdapterLeaderBoard: LeaderBoard_Adapter
    private var _binding: FragmentLeaderBoardBinding? = null
    private val binding get() = _binding!!
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLeaderBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            backIntent()
        }

        firestore = FirebaseFirestore.getInstance()
        val dataList = ArrayList<newUser>()
        val newList = ArrayList<newUser>()

        binding.topUserName.text = ""
        binding.topUserName2.text = ""
        binding.topUserName3.text = ""

        rvAdapterLeaderBoard = LeaderBoard_Adapter(newList, requireContext())
        binding.RvLeaderBoard.adapter = rvAdapterLeaderBoard
        binding.RvLeaderBoard.layoutManager = LinearLayoutManager(requireContext())

        firestore.collection("users")
            .orderBy("coins", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { queryDocumentSnapshots: QuerySnapshot ->
                for (snapshot in queryDocumentSnapshots) {
                    val user = snapshot.toObject(newUser::class.java)
                    dataList.add(user)
                }
                // Sort the list in descending order.
                dataList.sortByDescending { it.getCoins() }
                val topThreeUsers = dataList.take(3)
                newList.addAll(dataList.subList(3, dataList.size))
                dataList.removeAll(topThreeUsers)

                if (topThreeUsers.size >= 1) {
                    binding.topUserName.text = topThreeUsers[0].getName()
                    if (topThreeUsers[0].getProfile() == null){
                        Glide.with(requireContext())
                            .load(R.drawable.demo_user)
                            .into(binding.topUserProfile)
                    }else{
                        Glide.with(requireContext())
                            .load(topThreeUsers[0].getProfile())
                            .into(binding.topUserProfile)
                    }
                }
                if (topThreeUsers.size >= 2) {
                    binding.topUserName2.text = topThreeUsers[1].getName()
                    if (topThreeUsers[1].getProfile() == null){
                        Glide.with(requireContext())
                            .load(R.drawable.demo_user)
                            .into(binding.topUserProfile2)
                    }else{
                        Glide.with(requireContext())
                            .load(topThreeUsers[1].getProfile())
                            .into(binding.topUserProfile2)
                    }
                }
                if (topThreeUsers.size >= 3) {
                    binding.topUserName3.text = topThreeUsers[2].getName()
                    if (topThreeUsers[2].getProfile() == null){
                        Glide.with(requireContext())
                            .load(R.drawable.demo_user)
                            .into(binding.topUserProfile3)
                    }else{
                        Glide.with(requireContext())
                            .load(topThreeUsers[2].getProfile())
                            .into(binding.topUserProfile3)
                    }
                }

                rvAdapterLeaderBoard.notifyDataSetChanged()
            }
    }

    private fun backIntent() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
    }
}