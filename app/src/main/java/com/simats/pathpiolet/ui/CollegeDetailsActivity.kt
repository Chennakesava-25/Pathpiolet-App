package com.simats.pathpiolet.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.simats.pathpiolet.R
import com.simats.pathpiolet.data.College
import com.simats.pathpiolet.data.SavedItemManager
import com.simats.pathpiolet.databinding.ActivityCollegeDetailsBinding
import androidx.core.content.ContextCompat

class CollegeDetailsActivity : BaseActivity() {

    private lateinit var binding: ActivityCollegeDetailsBinding
    private var college: College? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCollegeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Receive data
        // Receive data using type-safe method
        college = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("college_data", College::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("college_data")
        }

        setupUI()
        setupClickListeners()
    }

    private fun setupUI() {
        college?.let {
            binding.tvCollegeName.text = it.name ?: "N/A"
            binding.tvLocation.text = "${it.city ?: "N/A"}, ${it.state ?: ""}"
            
            if (it.nirfRank > 0) {
                binding.badgeRank.visibility = android.view.View.VISIBLE
                binding.badgeRank.text = "#${it.nirfRank} NIRF Ranking"
            } else {
                binding.badgeRank.visibility = android.view.View.GONE
            }
            
            if (it.score > 0) {
                binding.badgeScore.visibility = android.view.View.VISIBLE
                binding.badgeScore.text = "Score: ${it.score}"
            } else {
                binding.badgeScore.visibility = android.view.View.GONE
            }
            
            // Set Stats using included layout ids
            // With ViewBinding, included layouts with IDs become properties of the specific binding type.
            binding.statFees.tvStatLabel.text = "Total Fees"
            binding.statFees.tvStatValue.text = it.fees ?: "N/A"
            
            binding.statPackage.tvStatLabel.text = "Avg Package"
            binding.statPackage.tvStatValue.text = it.avgPackage ?: "N/A"

            binding.tvAbout.text = if (!it.description.isNullOrEmpty()) {
                it.description
            } else {
                "Information about ${it.name} is currently being updated. Please check the official website for details."
            }
            
            // Toggle website visibility
            binding.tvWebsite.visibility = if (it.website.isNullOrEmpty()) android.view.View.GONE else android.view.View.VISIBLE
            
            updateSaveIcon()
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.root.setOnClickListener { finish() }
        
        binding.btnSave.setOnClickListener {
            college?.let { c ->
                val userId = com.simats.pathpiolet.utils.SessionManager(this).getUserId()
                 if (SavedItemManager.isSaved(this, c)) {
                    SavedItemManager.removeCollege(this, userId, c)
                    c.isSaved = false
                    Toast.makeText(this, "Removed from Saved", Toast.LENGTH_SHORT).show()
                } else {
                    SavedItemManager.saveCollege(this, userId, c)
                    c.isSaved = true
                    Toast.makeText(this, "College Saved Successfully", Toast.LENGTH_SHORT).show()
                }
                updateSaveIcon()
            }
        }
        
        binding.tvWebsite.setOnClickListener {
            var url = college?.website
            if (!url.isNullOrEmpty()) {
                // Ensure URL has a scheme
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "https://$url"
                }
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(this, "Could not open website", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Website not available", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun updateSaveIcon() {
        college?.let {
             if (SavedItemManager.isSaved(this, it)) {
                 binding.btnSave.setImageResource(android.R.drawable.star_big_on)
                 binding.btnSave.setColorFilter(ContextCompat.getColor(this, R.color.splash_primary))
            } else {
                 binding.btnSave.setImageResource(android.R.drawable.star_big_off)
                 binding.btnSave.setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray))
            }
        }
    }
}
