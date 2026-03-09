package com.simats.pathpiolet.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip
import com.simats.pathpiolet.R
import com.simats.pathpiolet.databinding.ActivityGraduationBinding

class GraduationActivity : BaseActivity() {

    private lateinit var binding: ActivityGraduationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGraduationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        // Default to B.Tech
        showBTechContent()
    }

    private fun setupListeners() {
        binding.btnBack.root.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnNext.setOnClickListener {
            try {
                val intent = Intent(this, MastersActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            } catch (e: Exception) {
                // Activity might not exist yet
            }
        }

        binding.toggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.btnBTech -> showBTechContent()
                    R.id.btnBSc -> showBScContent()
                }
            }
        }
    }

    private fun showBTechContent() {
        // Duration Card
        binding.cardDuration.background = ContextCompat.getDrawable(this, R.drawable.bg_btech_duration)
        binding.tvDurationValue.text = "4 Years"

        // Focus Items
        binding.layoutFocusItems.removeAllViews()
        addFocusItem("Theory + Projects", R.color.focus_bg_blue)
        addFocusItem("University exams", R.color.focus_bg_blue)

        // Specializations
        binding.chipGroupSpecializations.removeAllViews()
        addChip("AI & ML", "#4D6FFF")
        addChip("Data Science", "#8A70FF")
        addChip("Cyber Security", "#FFB300")
        addChip("Cloud Computing", "#00BFA5")

        // Career Outcomes
        binding.layoutCareerOutcomes.removeAllViews()
        addCareerOutcome("Software Developer", R.drawable.ic_briefcase)
        addCareerOutcome("AI Engineer", R.drawable.ic_medal)
        addCareerOutcome("Data Scientist", R.drawable.ic_trending_up)
        addCareerOutcome("App Developer", R.drawable.ic_school)
    }

    private fun showBScContent() {
        // Duration Card
        binding.cardDuration.background = ContextCompat.getDrawable(this, R.drawable.bg_bsc_duration)
        binding.tvDurationValue.text = "3 Years"

        // Focus Items
        binding.layoutFocusItems.removeAllViews()
        addFocusItem("Theory focused", R.color.focus_bg_yellow)
        addFocusItem("Solid fundamentals", R.color.focus_bg_yellow)
        addFocusItem("Research preparation", R.color.focus_bg_yellow)

        // Specializations
        binding.chipGroupSpecializations.removeAllViews()
        addChip("Data Science", "#FFB300")
        addChip("AI", "#8A70FF")
        addChip("Advanced Computing", "#4D6FFF")

        // Career Outcomes
        binding.layoutCareerOutcomes.removeAllViews()
        addCareerOutcome("Software Developer", R.drawable.ic_briefcase)
        addCareerOutcome("Data Scientist", R.drawable.ic_trending_up)
        addCareerOutcome("University Lecturer", R.drawable.ic_school)
        addCareerOutcome("Research Assistant", R.drawable.ic_research)
    }

    private fun addFocusItem(text: String, bgColorRes: Int) {
        val view = TextView(this)
        view.text = "•  $text"
        view.setTextColor(ContextCompat.getColor(this, R.color.roadmap_primary))
        view.textSize = 14f
        view.setPadding(32, 24, 32, 24)
        
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 0, 0, 16)
        view.layoutParams = params
        
        // Use a drawable for background with rounded corners
        val drawable = if (bgColorRes == R.color.focus_bg_blue) 
            R.drawable.bg_focus_item_btech 
        else 
            R.drawable.bg_focus_item_bsc
            
        view.background = ContextCompat.getDrawable(this, drawable)
        
        binding.layoutFocusItems.addView(view)
    }

    private fun addChip(text: String, colorHex: String) {
        val chip = Chip(this)
        chip.text = text
        chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(colorHex)))
        chip.setTextColor(Color.WHITE)
        chip.textSize = 12f
        binding.chipGroupSpecializations.addView(chip)
    }

    private fun addCareerOutcome(title: String, iconRes: Int) {
        val view = LayoutInflater.from(this).inflate(R.layout.item_career_outcome_card, binding.layoutCareerOutcomes, false)
        val tvRole = view.findViewById<TextView>(R.id.tvRole)
        val imgIcon = view.findViewById<ImageView>(R.id.imgIcon) // Use imgIcon ID from item layout

        tvRole.text = title
        imgIcon.setImageResource(iconRes)
        
        // Slight tint adjustment for different icons if needed, or keep unified
        // imgIcon.setColorFilter(ContextCompat.getColor(this, R.color.roadmap_primary))

        binding.layoutCareerOutcomes.addView(view)
    }
}
