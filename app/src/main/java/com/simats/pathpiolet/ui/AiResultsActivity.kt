package com.simats.pathpiolet.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.simats.pathpiolet.data.College
import com.simats.pathpiolet.databinding.ActivityAiResultsBinding
import com.simats.pathpiolet.ui.adapter.AiCollegeAdapter

import android.util.Log
import android.widget.Toast
import com.simats.pathpiolet.api.RecommendationsRequest
import com.simats.pathpiolet.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AiResultsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAiResultsBinding
    private lateinit var adapter: AiCollegeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAiResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        fetchRecommendations()
    }

    private fun fetchRecommendations() {
        val location = intent.getStringExtra("location") ?: ""
        val budget = intent.getIntExtra("budget", 500000)
        val collegeTypes = intent.getStringArrayListExtra("collegeTypes") ?: arrayListOf()
        val hostel = intent.getBooleanExtra("hostel", false)
        val placementPriority = intent.getStringExtra("placementPriority") ?: "Medium"
        val campusSize = intent.getStringExtra("campusSize") ?: ""
        val examScore = intent.getStringExtra("examScore") ?: ""
        val specializations = intent.getStringArrayListExtra("specializations") ?: arrayListOf()

        val request = RecommendationsRequest(
            location = location,
            budget = budget,
            collegeTypes = collegeTypes,
            hostel = hostel,
            placementPriority = placementPriority,
            campusSize = campusSize,
            examScore = examScore,
            specializations = specializations
        )

        RetrofitClient.instance.getRecommendations(request).enqueue(object : Callback<List<College>> {
            override fun onResponse(call: Call<List<College>>, response: Response<List<College>>) {
                if (response.isSuccessful) {
                    val colleges = response.body() ?: emptyList()
                    setupRecyclerView(colleges)
                } else {
                    Toast.makeText(this@AiResultsActivity, "Failed to get recommendations", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<College>>, t: Throwable) {
                Log.e("AiResultsActivity", "Error: ${t.message}")
                Toast.makeText(this@AiResultsActivity, "Error connecting to server", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupRecyclerView(colleges: List<College>) {
        adapter = AiCollegeAdapter(colleges, onCollegeClick = { college ->
             val intent = Intent(this, CollegeDetailsActivity::class.java)
             intent.putExtra("college_data", college)
             startActivity(intent)
        })
        binding.rvAiResults.layoutManager = LinearLayoutManager(this)
        binding.rvAiResults.adapter = adapter
    }
}
