package com.hello_groups.quizai.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.hello_groups.quizai.Activities.MainActivity
import com.hello_groups.quizai.Activities.WalletActivity
import com.hello_groups.quizai.Models.newUser
import com.hello_groups.quizai.R
import com.hello_groups.quizai.databinding.FragmentProfileBinding
import de.hdodenhof.circleimageview.CircleImageView

class ProfileFragment : Fragment() {
    private lateinit var _binding: FragmentProfileBinding
    private val binding get() = _binding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var name:String


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        val currentUser: FirebaseUser? = auth.currentUser

        binding.backBtn.setOnClickListener {
            backIntent()
        }

        firestore.collection("users")
            .document(currentUser?.uid ?: "")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Handle error if needed
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    val user = snapshot.toObject(newUser::class.java)
                    name = user?.getName().toString()
                    binding.name.text = name
                    binding.coins.text = user?.getCoins().toString()
                    val context = requireContext()
                    if (user?.getProfile() == null) {
                        Glide.with(context)
                            .load(R.drawable.demo_user)
                            .into(binding.userProfile)
                    } else {
                        Glide.with(context)
                            .load(user.getProfile())
                            .into(binding.userProfile)
                    }
                }
            }

        binding.walletBtn.setOnClickListener {
            startActivity(Intent(requireContext(),WalletActivity::class.java))
        }

    }

    private fun backIntent() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
    }

    companion object {
    }
}

