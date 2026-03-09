package com.simats.pathpiolet.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.simats.pathpiolet.databinding.ActivityPreferencesBinding
import com.google.android.material.chip.Chip

class PreferencesActivity : BaseActivity() {

    private lateinit var binding: ActivityPreferencesBinding
    private var path: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityPreferencesBinding.inflate(layoutInflater)
            setContentView(binding.root)
            
            path = intent.getStringExtra("path")
            setupUI()
        } catch (e: Exception) {
            Log.e("PreferencesActivity", "Error in onCreate", e)
            Toast.makeText(this, "Error initializing preferences", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun setupUI() {
        binding.btnBack.root.setOnClickListener { finish() }
        
        // Budget Slider initialization and display update
        binding.sliderBudget.value = 150000f
        binding.tvBudget.text = "₹150K"
        binding.sliderBudget.addOnChangeListener { _, value, _ ->
            binding.tvBudget.text = "₹${(value / 1000).toInt()}K"
        }

        // Placement Priority Slider display update
        binding.sliderPlacementPriority.addOnChangeListener { _, value, _ ->
            val priority = when(value.toInt()) {
                0 -> "Low"
                1 -> "Medium"
                else -> "High"
            }
            binding.tvPlacementPriorityLabel.text = "Placement Priority: $priority"
        }

        // Handle College Type button mutual exclusivity
        binding.btnTypeAny.setOnClickListener {
            // Note: MaterialButton with checkable=true toggles automatically on click
            if (binding.btnTypeAny.isChecked) {
                binding.btnTypeGov.isChecked = false
                binding.btnTypePrivate.isChecked = false
                binding.btnTypeDeemed.isChecked = false
            }
        }
        
        binding.btnTypeGov.setOnClickListener {
            if (binding.btnTypeGov.isChecked) {
                binding.btnTypeAny.isChecked = false
            }
        }
        
        binding.btnTypePrivate.setOnClickListener {
            if (binding.btnTypePrivate.isChecked) {
                binding.btnTypeAny.isChecked = false
            }
        }
        
        binding.btnTypeDeemed.setOnClickListener {
            if (binding.btnTypeDeemed.isChecked) {
                binding.btnTypeAny.isChecked = false
            }
        }

        binding.btnGetRecommendations.setOnClickListener {
            if (isFinishing || isDestroyed) return@setOnClickListener

            try {
                Log.d("PreferencesActivity", "Collecting preferences...")
                val location = binding.etLocation.text?.toString() ?: ""
                val budget = binding.sliderBudget.value.toInt()
                Log.d("PreferencesActivity", "Budget: $budget")
                
                // Collect selected college types from buttons
                val selectedCollegeTypes = ArrayList<String>()
                if (binding.btnTypeGov.isChecked) selectedCollegeTypes.add("Government")
                if (binding.btnTypePrivate.isChecked) selectedCollegeTypes.add("Private")
                if (binding.btnTypeDeemed.isChecked) selectedCollegeTypes.add("Deemed")

                val hostelRequired = binding.switchHostel.isChecked
                
                val placementPriority = when(binding.sliderPlacementPriority.value.toInt()) {
                    0 -> "Low"
                    1 -> "Medium"
                    else -> "High"
                }

                var selectedCampusSize = ""
                val campusId = binding.chipGroupCampus.checkedChipId
                if (campusId != -1) {
                    try {
                        val chip = binding.root.findViewById<Chip>(campusId)
                        selectedCampusSize = chip?.text?.toString() ?: ""
                    } catch (e: Exception) {
                        Log.e("PreferencesActivity", "Error getting campus chip $campusId", e)
                    }
                }

                val examScore = binding.etExamScore.text?.toString() ?: ""

                val selectedSpecializations = ArrayList<String>()
                binding.chipGroupSpecialization.checkedChipIds.forEach { id ->
                    try {
                        val chip = binding.root.findViewById<Chip>(id)
                        chip?.text?.toString()?.let { selectedSpecializations.add(it) }
                    } catch (e: Exception) {
                        Log.e("PreferencesActivity", "Error getting spec chip $id", e)
                    }
                }

                Log.d("PreferencesActivity", "Collected all. Starting AiProcessingActivity...")

                // Navigate to AI Processing
                val intent = Intent(this, AiProcessingActivity::class.java).apply {
                    putExtra("location", location)
                    putExtra("budget", budget)
                    putStringArrayListExtra("collegeTypes", selectedCollegeTypes)
                    putExtra("hostel", hostelRequired)
                    putExtra("placementPriority", placementPriority)
                    putExtra("campusSize", selectedCampusSize)
                    putExtra("examScore", examScore)
                    putStringArrayListExtra("specializations", selectedSpecializations)
                    putExtra("path", path)
                }
                startActivity(intent)
                Log.d("PreferencesActivity", "AiProcessingActivity started.")
            } catch (e: Exception) {
                Log.e("PreferencesActivity", "CRASH in click listener", e)
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
