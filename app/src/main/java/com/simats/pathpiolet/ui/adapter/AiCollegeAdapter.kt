package com.simats.pathpiolet.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.simats.pathpiolet.data.College
import com.simats.pathpiolet.data.SavedItemManager
import com.simats.pathpiolet.databinding.ItemAiCollegeBinding
import android.graphics.Color
import com.simats.pathpiolet.R

class AiCollegeAdapter(
    private val colleges: List<College>,
    private val onCollegeClick: (College) -> Unit
) : RecyclerView.Adapter<AiCollegeAdapter.AiCollegeViewHolder>() {

    inner class AiCollegeViewHolder(val binding: ItemAiCollegeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AiCollegeViewHolder {
        val binding = ItemAiCollegeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AiCollegeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AiCollegeViewHolder, position: Int) {
        val college = colleges.getOrNull(holder.bindingAdapterPosition) ?: return
        with(holder.binding) {
            tvCollegeName.text = college.name ?: "N/A"
            tvLocation.text = "${college.city ?: "N/A"}, ${college.state ?: ""}"
            tvFees.text = college.fees?.replace("/yr", "") ?: "N/A"
            tvPackage.text = college.avgPackage ?: "N/A"
            badgeMatch.text = "${college.matchScore}% Match"
            
            // NIRF Rank Badge
            if (college.nirfRank > 0) {
                tvRankBadge.visibility = android.view.View.VISIBLE
                tvRankBadge.text = "NIRF Rank: #${college.nirfRank}"
            } else {
                tvRankBadge.visibility = android.view.View.GONE
            }
            
            // Render Tags dynamically
            chipGroupTags.removeAllViews()
            college.tags?.forEach { tag ->
                if (tag.isNullOrBlank()) return@forEach
                val chip = com.google.android.material.chip.Chip(root.context).apply {
                    text = tag
                    isClickable = false
                    isCheckable = false
                    textSize = 12f
                }
                chipGroupTags.addView(chip)
            }

            // Set Match Badge Color based on score
            val matchColor = if (college.matchScore >= 90) "#3D5AFE" else if (college.matchScore >= 80) "#536DFE" else "#8C9EFF"
            badgeMatch.background.setTint(Color.parseColor(matchColor))

            // Update Save Icon
            updateSaveIcon(this, college)

            btnViewDetails.setOnClickListener { onCollegeClick(college) }
            
            btnSave.setOnClickListener {
                val userId = com.simats.pathpiolet.utils.SessionManager(root.context).getUserId()
                if (SavedItemManager.isSaved(root.context, college)) {
                    SavedItemManager.removeCollege(root.context, userId, college)
                    college.isSaved = false
                    Toast.makeText(root.context, "Removed from Saved", Toast.LENGTH_SHORT).show()
                } else {
                    SavedItemManager.saveCollege(root.context, userId, college)
                    college.isSaved = true
                    Toast.makeText(root.context, "College Saved Successfully", Toast.LENGTH_SHORT).show()
                }
                updateSaveIcon(this, college)
            }
        }
    }
    
    // Helper to update save icon visual state
    private fun updateSaveIcon(binding: ItemAiCollegeBinding, college: College) {
        val isSaved = SavedItemManager.isSaved(binding.root.context, college)
        // Note: Using ic_search as placeholder for bookmark if not available, ideally should be bookmark icon
        // Assuming we need to use existing or standard drawable. 
        // Using localized check
        if (isSaved) {
             binding.btnSave.setIconResource(android.R.drawable.btn_star_big_on)
             binding.btnSave.setIconTintResource(R.color.splash_primary)
        } else {
             binding.btnSave.setIconResource(android.R.drawable.btn_star_big_off)
             binding.btnSave.setIconTintResource(android.R.color.darker_gray)
        }
    }

    override fun getItemCount() = colleges.size
}
