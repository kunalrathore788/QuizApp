package com.hello_groups.quizai.Activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.hello_groups.quizai.Adapters.Categories_Adapter
import com.hello_groups.quizai.Constant
import com.hello_groups.quizai.Models.Categories_Model
import com.hello_groups.quizai.databinding.ActivityCategoriesBinding

class Categories_Activity : AppCompatActivity() {
    private lateinit var database : FirebaseFirestore
    private lateinit var binding: ActivityCategoriesBinding
    private lateinit var rvadapter: Categories_Adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseFirestore.getInstance()

        val categories = ArrayList<Categories_Model>()
        rvadapter = Categories_Adapter(categories,this)
        val categoriesCollection = FirebaseFirestore.getInstance().collection("categories")
        val eventListener = EventListener<QuerySnapshot> { value, error ->
            categories.clear()
            value?.let {
                for (snapshot in it.documents) {
                    val model = snapshot.toObject(Categories_Model::class.java)
                    model?.setCategoryId(snapshot.id)
                    model?.let { categoryModel ->
                        categories.add(categoryModel)
                    }
                }
            }
            rvadapter.notifyDataSetChanged()
        }
        categoriesCollection.addSnapshotListener(eventListener)
        val numberOfColumns = 2 // Define the number of columns in the grid
        binding.CategoriesRv.layoutManager = GridLayoutManager(this, numberOfColumns)
        binding.CategoriesRv.adapter = rvadapter

        binding.backBtn.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}