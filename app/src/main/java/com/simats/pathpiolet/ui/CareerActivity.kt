package com.simats.pathpiolet.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.simats.pathpiolet.R
import com.simats.pathpiolet.databinding.ActivityCareerBinding

class CareerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCareerBinding
    private lateinit var adapter: JobAdapter
    private var isBTechSelected = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCareerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupListeners()
        updateData()
    }

    private fun setupUI() {
        binding.rvJobs.layoutManager = LinearLayoutManager(this)
        adapter = JobAdapter(emptyList())
        binding.rvJobs.adapter = adapter
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnBTech.setOnClickListener {
            if (!isBTechSelected) {
                isBTechSelected = true
                updateToggleState()
                updateData()
            }
        }

        binding.btnBSc.setOnClickListener {
            if (isBTechSelected) {
                isBTechSelected = false
                updateToggleState()
                updateData()
            }
        }

        binding.btnViewRoadmap.setOnClickListener {
             // Navigate back to Roadmap Screen (MainActivity with extra)
             // Or just finish() if opened from Roadmap
             finish()
        }
    }

    private fun updateToggleState() {
        if (isBTechSelected) {
            // Select B.Tech (Blue)
            binding.btnBTech.background = ContextCompat.getDrawable(this, R.drawable.bg_toggle_selected_blue)
            binding.btnBTech.setTextColor(Color.WHITE)
            
            binding.btnBSc.background = null
            binding.btnBSc.setTextColor(Color.parseColor("#7C86A2"))

            // Update Tips Card Style for Blue
             binding.cardTips.setCardBackgroundColor(Color.parseColor("#E8F0FF")) // Light Blue
             binding.ivTipIcon.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
             binding.ivTipIcon.imageTintList = ColorStateList.valueOf(Color.parseColor("#4A69FF"))
             binding.btnViewRoadmap.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#00C853")) // Keep green or change?
             
        } else {
            // Select B.Sc (Yellow/Gold)
            binding.btnBSc.background = ContextCompat.getDrawable(this, R.drawable.bg_toggle_selected_yellow) // Need to create this or use simple shape
            // For now let's reuse blue or set color filter, but creating a drawable is better. 
            // I'll set background manually if drawable is missing, or create it.
            // Let's assume I need to create `bg_toggle_selected_yellow`.
            // Or just set tint if it's a shape.
            // Let's try dynamic tinting
            
            val yellowDrawable = ContextCompat.getDrawable(this, R.drawable.bg_toggle_selected_blue)?.mutate()
            yellowDrawable?.setTint(Color.parseColor("#FFC107")) // Amber/Yellow
            binding.btnBSc.background = yellowDrawable
            
            binding.btnBSc.setTextColor(Color.WHITE)

            binding.btnBTech.background = null
            binding.btnBTech.setTextColor(Color.parseColor("#7C86A2"))
            
             // Update Tips Card Style for Yellow
             binding.cardTips.setCardBackgroundColor(Color.parseColor("#FFF8E1")) // Light Yellow
             binding.ivTipIcon.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
             binding.ivTipIcon.imageTintList = ColorStateList.valueOf(Color.parseColor("#FFC107"))
        }
    }

    private fun updateData() {
        val jobs = if (isBTechSelected) {
            getBTechJobs()
        } else {
            getBScJobs()
        }
        adapter.updateList(jobs)
        
        // Update Tips Text
        if (isBTechSelected) {
             binding.tvTipDescription.text = "B.Tech graduates have strong industry placements with higher starting salaries. Focus on building projects and participating in hackathons."
        } else {
             binding.tvTipDescription.text = "B.Sc graduates excel in research and academics. Consider pursuing M.Sc or M.Tech for advanced career opportunities."
        }
    }

    private fun getBTechJobs() = listOf(
        JobModule("Software Developer", "AI/ML Engineer", R.drawable.ic_laptop, "#E8F0FF", "#4A69FF"), // Blue
        JobModule("Web Developer", "Cyber Security Analyst", R.drawable.ic_globe, "#F3E5F5", "#9C27B0"), // Purple
        JobModule("Data Scientist", "Advanced Algorithms", R.drawable.ic_chart, "#FFF8E1", "#FFC107"), // Yellow
        JobModule("App Developer", "Mobile & Cross-platform", R.drawable.ic_mobile, "#E8F5E9", "#4CAF50"), // Green
        JobModule("DevOps Engineer", "CI/CD & Automation", R.drawable.ic_gear, "#FFF3E0", "#FF9800"), // Orange
        JobModule("Cloud Engineer", "AWS, Azure, GCP", R.drawable.ic_cloud, "#E0F7FA", "#00BCD4")  // Cyan
    )

    private fun getBScJobs() = listOf(
        JobModule("Software Developer", "Application Development", R.drawable.ic_laptop, "#E8F0FF", "#4A69FF"),
        JobModule("Web Developer", "Frontend & Backend", R.drawable.ic_globe, "#F3E5F5", "#9C27B0"),
        JobModule("Data Analyst", "Business Intelligence", R.drawable.ic_chart, "#FFF8E1", "#FFC107"),
        JobModule("System Analyst", "IT Solutions", R.drawable.ic_monitor, "#E8F5E9", "#4CAF50"),
        JobModule("IT Support Specialist", "Technical Support", R.drawable.ic_tools, "#FFF3E0", "#FF9800"),
        JobModule("Research Assistant", "Academic Research", R.drawable.ic_microscope, "#F3E5F5", "#9C27B0")
    )

    data class JobModule(
        val title: String, 
        val subtitle: String, 
        val iconResId: Int, 
        val bgHexColor: String, 
        val iconTintHexColor: String
    )
    
    inner class JobAdapter(private var items: List<JobModule>) : RecyclerView.Adapter<JobAdapter.ViewHolder>() {
        
        fun updateList(newItems: List<JobModule>) {
            items = newItems
            notifyDataSetChanged()
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvTitle: TextView = view.findViewById(R.id.tvJobTitle)
            val tvSubtitle: TextView = view.findViewById(R.id.tvJobSubtitle)
            val ivIcon: ImageView = view.findViewById(R.id.ivJobIcon)
            val iconContainer: FrameLayout = view.findViewById(R.id.iconContainer)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_career_job, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.tvTitle.text = item.title
            holder.tvSubtitle.text = item.subtitle
            holder.ivIcon.setImageResource(item.iconResId)
            
            // Dynamic Colors
            try {
                val colorStateList = ColorStateList.valueOf(Color.parseColor(item.bgHexColor))
                holder.iconContainer.backgroundTintList = colorStateList
                
                holder.ivIcon.imageTintList = ColorStateList.valueOf(Color.parseColor(item.iconTintHexColor))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun getItemCount() = items.size
    }
}
