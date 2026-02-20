package com.simats.pathpiolet.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.simats.pathpiolet.R
import com.simats.pathpiolet.databinding.ActivityCareerSuccessBinding

class CareerSuccessActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCareerSuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCareerSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        showBTechContent() // Default view
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnFullRoadmap.setOnClickListener {
            // Navigate back to Roadmap or clear stack to Roadmap
            val intent = Intent(this, RoadmapActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
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
        binding.layoutJobList.removeAllViews()
        
        addJobItem("Software Developer", "AI/ML Engineer", R.color.job_icon_bg_blue, R.drawable.ic_laptop)
        addJobItem("Web Developer", "Cyber Security Analyst", R.color.job_icon_bg_purple, R.drawable.ic_globe)
        addJobItem("Data Scientist", "Advanced Algorithms", R.color.job_icon_bg_yellow, R.drawable.ic_chart)
        addJobItem("App Developer", "Mobile & Cross-platform", R.color.job_icon_bg_green, R.drawable.ic_mobile)
        addJobItem("DevOps Engineer", "CI/CD & Automation", R.color.job_icon_bg_orange, R.drawable.ic_gear)
        addJobItem("Cloud Engineer", "AWS, Azure, GCP", R.color.job_icon_bg_cyan, R.drawable.ic_cloud)

        binding.tvCareerTip.text = "B.Tech graduates have strong industry placements with higher starting salaries. Focus on building projects and participating in hackathons."
    }

    private fun showBScContent() {
        binding.layoutJobList.removeAllViews()

        addJobItem("Software Developer", "Application Development", R.color.job_icon_bg_blue, R.drawable.ic_laptop)
        addJobItem("Web Developer", "Frontend & Backend", R.color.job_icon_bg_purple, R.drawable.ic_globe)
        addJobItem("Data Analyst", "Business Intelligence", R.color.job_icon_bg_yellow, R.drawable.ic_chart)
        addJobItem("System Analyst", "IT Solutions", R.color.job_icon_bg_green, R.drawable.ic_monitor)
        addJobItem("IT Support Specialist", "Technical Support", R.color.job_icon_bg_orange, R.drawable.ic_tools)
        addJobItem("Research Assistant", "Academic Research", R.color.job_icon_bg_purple, R.drawable.ic_microscope)

        binding.tvCareerTip.text = "B.Sc graduates excel in research and academics. Consider pursuing M.Sc or M.Tech for advanced career opportunities."
    }

    private fun addJobItem(title: String, subtitle: String, colorRes: Int, iconRes: Int) {
        val view = LayoutInflater.from(this).inflate(R.layout.item_job_card, binding.layoutJobList, false)
        
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val tvSubtitle = view.findViewById<TextView>(R.id.tvSubtitle)
        val imgIcon = view.findViewById<ImageView>(R.id.imgJobIcon)

        tvTitle.text = title
        tvSubtitle.text = subtitle
        imgIcon.setImageResource(iconRes)
        imgIcon.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, colorRes))

        binding.layoutJobList.addView(view)
    }
}
