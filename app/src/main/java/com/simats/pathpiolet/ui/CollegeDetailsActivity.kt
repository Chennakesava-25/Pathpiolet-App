package com.simats.pathpiolet.ui

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.simats.pathpiolet.R
import com.simats.pathpiolet.data.College
import com.simats.pathpiolet.data.SavedCollegeManager
import com.simats.pathpiolet.databinding.ActivityCollegeDetailsBinding

class CollegeDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCollegeDetailsBinding
    private var college: College? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCollegeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Receive data
        college = intent.getParcelableExtra("college_data")

        setupUI()
        setupClickListeners()
    }

    private fun setupUI() {
        college?.let {
            binding.tvCollegeName.text = it.name
            binding.tvLocation.text = "${it.city}, ${it.state}"
            binding.badgeRank.text = "#${it.rank} NIRF Ranking"
            binding.badgeScore.text = "Score: ${it.score}"
            
            // Set Stats using included layout ids
            // With ViewBinding, included layouts with IDs become properties of the specific binding type.
            binding.statFees.tvStatLabel.text = "Total Fees"
            binding.statFees.tvStatValue.text = it.fees
            
            binding.statPackage.tvStatLabel.text = "Avg Package"
            binding.statPackage.tvStatValue.text = it.avgPackage
            
            updateSaveIcon()
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener { finish() }
        
        binding.btnSave.setOnClickListener {
            college?.let { c ->
                 if (SavedCollegeManager.isSaved(this, c)) {
                    SavedCollegeManager.removeCollege(this, c)
                    c.isSaved = false
                    Toast.makeText(this, "Removed from Saved", Toast.LENGTH_SHORT).show()
                } else {
                    SavedCollegeManager.saveCollege(this, c)
                    c.isSaved = true
                    Toast.makeText(this, "College Saved Successfully", Toast.LENGTH_SHORT).show()
                }
                updateSaveIcon()
            }
        }
        
        binding.tvWebsite.setOnClickListener {
            // Open website intent
            Toast.makeText(this, "Opening Website...", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun updateSaveIcon() {
        college?.let {
             if (SavedCollegeManager.isSaved(this, it)) {
                 binding.btnSave.setImageResource(android.R.drawable.star_big_on)
                 binding.btnSave.setColorFilter(getColor(R.color.splash_primary))
            } else {
                 binding.btnSave.setImageResource(android.R.drawable.star_big_off)
                 binding.btnSave.setColorFilter(getColor(android.R.color.darker_gray))
            }
        }
    }
}
