package com.simats.pathpiolet.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.simats.pathpiolet.databinding.ActivityPreparationBinding

class PreparationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreparationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreparationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        markModuleAsInProgress()
        setupClickListeners()
    }

    private fun markModuleAsInProgress() {
        val prefs = getSharedPreferences("PathPioletPrefs", MODE_PRIVATE)
        val currentStatus = prefs.getString("PreparationModuleStatus", "Not Started")
        if (currentStatus != "Completed") {
            prefs.edit().putString("PreparationModuleStatus", "In Progress").apply()
        }
    }

    private fun markModuleAsCompleted() {
        val prefs = getSharedPreferences("PathPioletPrefs", MODE_PRIVATE)
        prefs.edit().putString("PreparationModuleStatus", "Completed").apply()
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(com.simats.pathpiolet.R.anim.slide_in_left, com.simats.pathpiolet.R.anim.slide_out_right)
        }

        // Entrance Exams
        binding.cardJee.setOnClickListener { navigateTo("ExamDetailActivity") }
        binding.cardEamcet.setOnClickListener { navigateTo("ExamDetailActivity") }
        binding.cardUniversityExams.setOnClickListener { navigateTo("ExamDetailActivity") }

        // Subjects
        binding.cardMaths.setOnClickListener { navigateTo("SubjectDetailActivity") }
        binding.cardPhysics.setOnClickListener { navigateTo("SubjectDetailActivity") }
        binding.cardChemistry.setOnClickListener { navigateTo("SubjectDetailActivity") }

        // Next Button
        binding.btnNextGraduation.setOnClickListener {
            markModuleAsCompleted()
            navigateTo("GraduationActivity")
        }
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
