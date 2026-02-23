package com.simats.pathpiolet.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.simats.pathpiolet.R
import com.simats.pathpiolet.databinding.ActivityPhdBinding

class PhdActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPhdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        setupResearchAreas()
        setupCareerPath()
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnCareer.setOnClickListener {
            try {
                val intent = Intent(this, CareerActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            } catch (e: Exception) {
                // Activity might not exist
            }
        }
    }

    private fun setupResearchAreas() {
        binding.layoutResearchAreas.removeAllViews()
        
        addResearchArea("Artificial Intelligence", "Neural networks, deep learning", R.color.research_ai_bg, R.drawable.ic_code) // Using generic icon if specific not available
        addResearchArea("Quantum Computing", "Quantum algorithms, quantum ML", R.color.research_quantum_bg, R.drawable.ic_cpu)
        addResearchArea("Cyber Security", "Cryptography, blockchain security", R.color.research_security_bg, R.drawable.ic_lock)
        addResearchArea("Data Mining", "Big data analytics, pattern recognition", R.color.research_data_bg, R.drawable.ic_trending_up)
        addResearchArea("Distributed Systems", "Cloud computing, edge computing", R.color.research_systems_bg, R.drawable.ic_server)
    }

    private fun setupCareerPath() {
        binding.layoutCareerPath.removeAllViews()

        addCareerPath("Professor / Lecturer", "Teaching & research at universities")
        addCareerPath("Research Scientist", "Leading research teams in institutes")
        addCareerPath("AI Researcher", "Research positions at Google, OpenAI")
        addCareerPath("Government Scientist", "ISRO, DRDO, CSIR labs")
    }

    private fun addResearchArea(title: String, subtitle: String, bgColorRes: Int, iconRes: Int) {
        val view = LayoutInflater.from(this).inflate(R.layout.item_research_area, binding.layoutResearchAreas, false)
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val tvSubtitle = view.findViewById<TextView>(R.id.tvSubtitle)
        val imgIcon = view.findViewById<ImageView>(R.id.imgIcon)
        // Root layout background tint would be better if dynamic, but here we set color to card/container
        // creating new drawable programmatically or just tinting background
        
        // For simplicity in this structure, we'll assume the item layout handles basic structure
        // But we want dynamic background colors. 
        // Let's set the background tint of the root view if it's a CardView or similar, or just set background color
        // The item layout `item_research_area.xml` root has `bg_research_step` (white). 
        // We want colored backgrounds as per screenshots.
        
        try {
            // Check if we can change background color directly
            view.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, bgColorRes))
        } catch (e: Exception) {
             view.setBackgroundColor(ContextCompat.getColor(this, bgColorRes))
        }

        tvTitle.text = title
        tvSubtitle.text = subtitle
        // imgIcon.setImageResource(iconRes) // Use specific icons if available, else keep default

        binding.layoutResearchAreas.addView(view)
    }

    private fun addCareerPath(title: String, desc: String) {
        val view = LayoutInflater.from(this).inflate(R.layout.item_career_path, binding.layoutCareerPath, false)
        val tvRole = view.findViewById<TextView>(R.id.tvRole)
        val tvDesc = view.findViewById<TextView>(R.id.tvDesc)
        
        tvRole.text = title
        tvDesc.text = desc
        
        binding.layoutCareerPath.addView(view)
    }
}
