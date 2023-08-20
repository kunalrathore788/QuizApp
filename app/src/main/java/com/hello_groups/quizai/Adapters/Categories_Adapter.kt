package com.hello_groups.quizai.Adapters

import android.content.Context
import android.content.Intent
import android.icu.text.Transliterator.Position
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.hello_groups.quizai.Activities.Quiz_Activity
import com.hello_groups.quizai.Models.Categories_Model
import com.hello_groups.quizai.databinding.CategoriesItemRvBinding

class Categories_Adapter(var dataList: ArrayList<Categories_Model>, val context: Context) : RecyclerView.Adapter<Categories_Adapter.CategoriesViewHolder> () {
    inner class CategoriesViewHolder (var binding: CategoriesItemRvBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val binding = CategoriesItemRvBinding.inflate(LayoutInflater.from(context), parent, false)
        return CategoriesViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val category = dataList[position]
        holder.binding.title.text = category.getTitle()
        // Load the image using Glide
        Glide.with(holder.itemView.context)
            .load(category.getIcon()) // Assuming imageId is the Int representing the image resource
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.binding.icon)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, Quiz_Activity::class.java)
            intent.putExtra("catId", category.getCategoryId())
            context.startActivity(intent)
        }
    }
}