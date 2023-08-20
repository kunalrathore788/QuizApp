package com.hello_groups.quizai.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hello_groups.quizai.Models.newUser
import com.hello_groups.quizai.R
import com.hello_groups.quizai.databinding.LeaderboardRvItemsBinding

class LeaderBoard_Adapter(var dataList : ArrayList<newUser>, val context: Context) : RecyclerView.Adapter<LeaderBoard_Adapter.LeaderBoardViewHolder>() {
    inner class LeaderBoardViewHolder(var binding: LeaderboardRvItemsBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderBoardViewHolder {
        val binding = LeaderboardRvItemsBinding.inflate(LayoutInflater.from(context),parent,false)
        return LeaderBoardViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: LeaderBoardViewHolder, position: Int) {
        val user = dataList[position]
        holder.binding.index.text = "#${position+4}"
        holder.binding.userName.text = user.getName()
        holder.binding.userScore.text = user.getCoins().toString()
        if (user.getProfile() == null){
            Glide.with(context)
                .load(R.drawable.user)
                .into(holder.binding.userProfile)
        }else{
            Glide.with(context)
                .load(user.getProfile())
                .into(holder.binding.userProfile)
        }
    }
}