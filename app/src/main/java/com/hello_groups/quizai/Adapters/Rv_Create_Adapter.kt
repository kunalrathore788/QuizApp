package com.hello_groups.quizai.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hello_groups.quizai.Models.Rv_Create_Model
import com.hello_groups.quizai.databinding.RvItemsBinding

class Rv_Create_Adapter(var dataList: ArrayList<Rv_Create_Model>, val context : Context):RecyclerView.Adapter<Rv_Create_Adapter.MyViewHolder>() {


    inner class MyViewHolder (var binding: RvItemsBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RvItemsBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.cardBg.setImageResource(dataList[position].card_Bg)
        holder.binding.cardH1.text = dataList[position].card_H1
        holder.binding.cardH2.text = dataList[position].card_H2
    }
}

