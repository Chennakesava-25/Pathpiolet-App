package com.simats.pathpiolet.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.simats.pathpiolet.databinding.ActivityFoundationBinding

class FoundationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFoundationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoundationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        markModuleAsViewed()
        setupClickListeners()
    }

    private fun markModuleAsViewed() {
        val prefs = getSharedPreferences("PathPioletPrefs", MODE_PRIVATE)
        prefs.edit().putBoolean("FoundationModuleViewed", true).apply()
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(com.simats.pathpiolet.R.anim.slide_in_left, com.simats.pathpiolet.R.anim.slide_out_right)
        }

        // Navigation to Graduation screens
        binding.cardBTech.setOnClickListener { navigateTo("GraduationActivity") }
        binding.cardBSc.setOnClickListener { navigateTo("GraduationActivity") }

        // Career Opportunity Clicks
        val careerClickListener = android.view.View.OnClickListener {
            navigateTo("CareerActivity")
        }

        binding.itemSoftwareDev.setOnClickListener(careerClickListener)
        binding.itemWebDev.setOnClickListener(careerClickListener)
        binding.itemDataAnalyst.setOnClickListener(careerClickListener)
        binding.itemAiMl.setOnClickListener(careerClickListener)
        binding.itemCyberSecurity.setOnClickListener(careerClickListener)
        binding.itemAppDev.setOnClickListener(careerClickListener)

        binding.btnExploreColleges.setOnClickListener {
            navigateTo("CollegeListingActivity")
        }
    }

    private fun navigateTo(activityName: String) {
        try {
            val intent = android.content.Intent(this, Class.forName("com.simats.pathpiolet.ui.$activityName"))
            startActivity(intent)
        } catch (e: Exception) {
            // Activity not yet implemented or error
        }
    }
}
