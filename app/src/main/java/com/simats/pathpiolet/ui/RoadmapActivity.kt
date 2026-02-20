package com.simats.pathpiolet.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.simats.pathpiolet.databinding.ActivityRoadmapBinding

class RoadmapActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRoadmapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoadmapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateModuleStates()
        setupClickListeners()
    }

    private fun updateModuleStates() {
        val prefs = getSharedPreferences("PathPioletPrefs", MODE_PRIVATE)
        
        // Foundation State
        val foundationViewed = prefs.getBoolean("FoundationModuleViewed", false)
        if (foundationViewed) {
            binding.tvFoundationProgress.text = "Viewed • 0 of 4 modules completed"
            binding.tvFoundationProgress.setTextColor(android.graphics.Color.parseColor("#4CAF50"))
        }

        // Preparation State
        val prepStatus = prefs.getString("PreparationModuleStatus", "Not Started")
        binding.tvPreparationProgress.text = prepStatus
        when (prepStatus) {
            "In Progress" -> binding.tvPreparationProgress.setTextColor(android.graphics.Color.parseColor("#FF9800"))
            "Completed" -> binding.tvPreparationProgress.setTextColor(android.graphics.Color.parseColor("#4CAF50"))
            else -> binding.tvPreparationProgress.setTextColor(android.graphics.Color.parseColor("#2196F3"))
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Navigation Helper
        val navigate = { activityName: String -> navigateTo(activityName) }

        // Step 1: Foundation
        binding.item10th.setOnClickListener { navigate("FoundationActivity") }
        binding.btn10th.setOnClickListener { navigate("FoundationActivity") }
        binding.icon10th.setOnClickListener { navigate("FoundationActivity") }

        // Step 2: Preparation
        binding.item12th.setOnClickListener { navigate("PreparationActivity") }
        binding.btn12th.setOnClickListener { navigate("PreparationActivity") }

        // Top Colleges
        binding.itemColleges.setOnClickListener { navigate("CollegesActivity") }
        binding.btnColleges.setOnClickListener { navigate("CollegesActivity") }

        // Step 3: Graduation
        binding.itemUg.setOnClickListener { navigate("GraduationActivity") }
        binding.btnUg.setOnClickListener { navigate("GraduationActivity") }

        // Step 4: Masters
        binding.itemMasters.setOnClickListener { navigate("MastersActivity") }
        binding.btnMasters.setOnClickListener { navigate("MastersActivity") }

        // Step 5: PhD
        binding.itemPhd.setOnClickListener { navigate("PhdActivity") }
        binding.btnPhd.setOnClickListener { navigate("PhdActivity") }

        // Step 6: Career
        binding.itemCareer.setOnClickListener { navigate("CareerSuccessActivity") }
        binding.btnCareer.setOnClickListener { navigate("CareerSuccessActivity") }
    }

    private fun navigateTo(activityName: String) {
        try {
            val intent = Intent(this, Class.forName("com.simats.pathpiolet.ui.$activityName"))
            startActivity(intent)
            overridePendingTransition(com.simats.pathpiolet.R.anim.slide_in_right, com.simats.pathpiolet.R.anim.slide_out_left)
        } catch (e: Exception) {
            // Activity not yet implemented
        }
    }
}
