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
import com.simats.pathpiolet.databinding.ActivityMastersBinding

class MastersActivity : BaseActivity() {

    private lateinit var binding: ActivityMastersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMastersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        // Default to M.Tech
        showMTechContent()
    }

    private fun setupListeners() {
        binding.btnBack.root.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnNext.setOnClickListener {
            try {
                val intent = Intent(this, PhdActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            } catch (e: Exception) {
                // Activity might not exist yet
            }
        }

        binding.toggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.btnMTech -> showMTechContent()
                    R.id.btnMSc -> showMScContent()
                }
            }
        }
    }

    private fun showMTechContent() {
        // Duration Card
        binding.cardDuration.background = ContextCompat.getDrawable(this, R.drawable.bg_mtech_duration)
        binding.tvDurationValue.text = "2 Years"
        binding.tvDurationSubtitle.text = "Engineering Focus"

        // Sections
        binding.tvFocusTitle.text = "Sections"
        binding.layoutFocusItems.removeAllViews()
        addFocusItem("Industry + Projects", R.color.focus_bg_purple)

        // Entrance Exams
        binding.tvExamTitle.text = "Entrance Exams"
        binding.layoutExamItems.removeAllViews()
        addExamItem("GATE", R.color.focus_bg_purple)
        addExamItem("University\nExams", R.color.focus_bg_purple)

        // Specializations
        binding.chipGroupSpecializations.removeAllViews()
        addChip("AI & ML", "#8A70FF")
        addChip("Data Science", "#4D6FFF")
        addChip("Cyber Security", "#FFB300")
        addChip("Cloud Computing", "#00BFA5")

        // Career Outcomes
        binding.layoutCareerOutcomes.removeAllViews()
        addCareerOutcome("Senior Engineer", R.drawable.ic_briefcase)
        addCareerOutcome("AI Engineer", R.drawable.ic_medal)
        addCareerOutcome("Data Scientist", R.drawable.ic_trending_up)
    }

    private fun showMScContent() {
        // Duration Card
        binding.cardDuration.background = ContextCompat.getDrawable(this, R.drawable.bg_msc_duration)
        binding.tvDurationValue.text = "2 Years"
        binding.tvDurationSubtitle.text = "Science Focus"

        // Focus
        binding.tvFocusTitle.text = "Focus"
        binding.layoutFocusItems.removeAllViews()
        addFocusItem("Theory", R.color.focus_bg_yellow_light)
        addFocusItem("Research", R.color.focus_bg_yellow_light)

        // Admission
        binding.tvExamTitle.text = "Admission"
        binding.layoutExamItems.removeAllViews()
        val view = TextView(this)
        view.text = "Merit-based admission"
        view.textSize = 14f
        view.setTextColor(ContextCompat.getColor(this, R.color.roadmap_primary))
        view.setPadding(32, 24, 32, 24)
        view.background = ContextCompat.getDrawable(this, R.drawable.bg_focus_item_msc)
        
        val params = LinearLayout.LayoutParams(
             LinearLayout.LayoutParams.MATCH_PARENT, 
             LinearLayout.LayoutParams.WRAP_CONTENT
        )
        view.layoutParams = params
        binding.layoutExamItems.addView(view)

        // Specializations
        binding.chipGroupSpecializations.removeAllViews()
        addChip("Data Science", "#FFB300")
        addChip("AI", "#8A70FF")
        addChip("Advanced Computing", "#4D6FFF")

        // Career Outcomes
        binding.layoutCareerOutcomes.removeAllViews()
        addCareerOutcome("University Lecturer", R.drawable.ic_school)
        addCareerOutcome("Data Scientist", R.drawable.ic_trending_up)
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
        val drawable = if (bgColorRes == R.color.focus_bg_purple) 
            R.drawable.bg_focus_item_mtech 
        else 
            R.drawable.bg_focus_item_msc
            
        view.background = ContextCompat.getDrawable(this, drawable)
        
        binding.layoutFocusItems.addView(view)
    }

    private fun addExamItem(text: String, bgColorRes: Int) {
        val view = TextView(this)
        view.text = text
        view.setTextColor(ContextCompat.getColor(this, R.color.roadmap_primary))
        view.textSize = 14f
        view.gravity = android.view.Gravity.CENTER
        view.setPadding(32, 24, 32, 24)
        
        val params = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        )
        params.setMargins(4, 0, 4, 0)
        view.layoutParams = params
        
        view.background = ContextCompat.getDrawable(this, R.drawable.bg_focus_item_mtech) // Reusing as similar style
        
        binding.layoutExamItems.addView(view)
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
        val imgIcon = view.findViewById<ImageView>(R.id.imgIcon)

        tvRole.text = title
        imgIcon.setImageResource(iconRes)

        binding.layoutCareerOutcomes.addView(view)
    }
}
