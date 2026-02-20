package com.simats.pathpiolet.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.simats.pathpiolet.data.College
import com.simats.pathpiolet.databinding.ActivityAiResultsBinding
import com.simats.pathpiolet.ui.adapter.AiCollegeAdapter

class AiResultsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAiResultsBinding
    private lateinit var adapter: AiCollegeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAiResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val dummyData = getDummyAiColleges()
        adapter = AiCollegeAdapter(dummyData, onCollegeClick = { college ->
             val intent = Intent(this, CollegeDetailsActivity::class.java)
             intent.putExtra("college_data", college)
             startActivity(intent)
        })
        binding.rvAiResults.layoutManager = LinearLayoutManager(this)
        binding.rvAiResults.adapter = adapter
    }

    private fun getDummyAiColleges(): List<College> {
        return listOf(
            College(
                rank = 1,
                name = "IIT Bangalore",
                city = "Bangalore",
                state = "Karnataka",
                score = 98.5,
                nirfRank = 2,
                fees = "₹ 2L/yr",
                avgPackage = "₹ 18 LPA",
                matchScore = 95,
                tags = listOf("Government", "Hostel"),
                instituteId = "IIT-BLR-001"
            ),
            College(
                rank = 5,
                name = "BITS Pilani",
                city = "Pilani",
                state = "Rajasthan",
                score = 92.0,
                nirfRank = 5,
                fees = "₹ 4.5L/yr",
                avgPackage = "₹ 16 LPA",
                matchScore = 92,
                tags = listOf("Deemed", "Hostel"),
                instituteId = "BITS-PIL-001"
            ),
            College(
                rank = 12,
                name = "VIT Vellore",
                city = "Vellore",
                state = "Tamil Nadu",
                score = 88.0,
                nirfRank = 12,
                fees = "₹ 1.95L/yr",
                avgPackage = "₹ 8 LPA",
                matchScore = 88,
                tags = listOf("Private", "Hostel"),
                instituteId = "VIT-VEL-001"
            ),
            College(
                rank = 8,
                name = "NIT Trichy",
                city = "Tiruchirappalli",
                state = "Tamil Nadu",
                score = 85.0,
                nirfRank = 9,
                fees = "₹ 1.25L/yr",
                avgPackage = "₹ 12 LPA",
                matchScore = 90,
                tags = listOf("Government", "Hostel"),
                instituteId = "NIT-TRY-001"
            ),
             College(
                rank = 55,
                name = "IIIT Hyderabad",
                city = "Hyderabad",
                state = "Telangana",
                score = 80.0,
                nirfRank = 55,
                fees = "₹ 3.0L/yr",
                avgPackage = "₹ 24 LPA",
                matchScore = 89,
                tags = listOf("Private", "Research"),
                instituteId = "IIIT-HYD-001"
            )
        )
    }
}
