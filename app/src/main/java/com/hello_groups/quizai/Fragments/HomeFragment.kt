package com.hello_groups.quizai.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.hello_groups.quizai.*
import com.hello_groups.quizai.Activities.*
import com.hello_groups.quizai.Adapters.Categories_Adapter
import com.hello_groups.quizai.Adapters.Rv_Create_Adapter
import com.hello_groups.quizai.Models.Categories_Model
import com.hello_groups.quizai.Models.newUser
import com.hello_groups.quizai.databinding.FragmentHomeBinding

class HomeFragment : Fragment(){
    private lateinit var database : FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var rvAdapter: Rv_Create_Adapter
    private lateinit var rvAdapterCategories: Categories_Adapter
    private lateinit var _binding: FragmentHomeBinding
    private val binding get() = _binding
    private lateinit var name:String
    private lateinit var user:newUser
    private lateinit var gestureDetector: GestureDetector

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser? = auth.currentUser

        binding.spinBtn.setOnClickListener {
            startActivity(Intent(requireContext(),SpinnerActivity::class.java))
        }

        database.collection("users")
            .document(currentUser?.uid ?: "")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Handle error if needed
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    val user = snapshot.toObject(newUser::class.java)
                    name = user?.getName().toString()
                    binding.userNameHome.text = name
                }
            }

        rvAdapter = Rv_Create_Adapter(Constant.getData(),requireContext())
        binding.createRv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false) // Set the layout manager
        binding.createRv.adapter = rvAdapter // Set your custom adapter

        gestureDetector = GestureDetector(requireContext(), object : GestureDetector.SimpleOnGestureListener() {
            override fun onLongPress(e: MotionEvent) {
                val childView = binding.createRv.findChildViewUnder(e.x, e.y)
                val position = childView?.let { binding.createRv.getChildAdapterPosition(it) }

                if (position != null && position != RecyclerView.NO_POSITION) {
                    when (position) {
                        0 -> {
                            val intent = Intent(requireContext(), VideoCall_Main_Activity::class.java)
                            requireContext().startActivity(intent)
                        }
                        1 -> {
//                            val intent = Intent(requireContext(), SecondOnBoardingActvity::class.java)
//                            requireContext().startActivity(intent)
                            Toast.makeText(requireContext(),"Available Soon",Toast.LENGTH_SHORT).show()
                        }
                        2 -> {
//                            val intent = Intent(requireContext(), VideoCall_Main_Activity::class.java)
//                            requireContext().startActivity(intent)
                            Toast.makeText(requireContext(),"Available Soon",Toast.LENGTH_SHORT).show()
                        }
                        // Add more cases for other positions as needed
                    }
                }
            }
        })

        binding.createRv.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                gestureDetector.onTouchEvent(e)
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })

        binding.seeAllBtn.setOnClickListener {
            val intent = Intent(requireContext(), Categories_Activity::class.java)
            requireContext().startActivity(intent)
        }

        val categories = ArrayList<Categories_Model>()
        rvAdapterCategories = Categories_Adapter(categories,requireContext())
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
            rvAdapterCategories.notifyDataSetChanged()
        }
        categoriesCollection.addSnapshotListener(eventListener)
        val numberOfColumns = 2 // Define the number of columns in the grid
        binding.CategoriesRv.layoutManager = GridLayoutManager(requireContext(), numberOfColumns)
        binding.CategoriesRv.adapter = rvAdapterCategories
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object {
    }

}