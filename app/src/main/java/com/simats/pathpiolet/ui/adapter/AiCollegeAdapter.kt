package com.simats.pathpiolet.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.simats.pathpiolet.data.College
import com.simats.pathpiolet.data.SavedCollegeManager
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
        val college = colleges[position]
        with(holder.binding) {
            tvCollegeName.text = college.name
            tvLocation.text = "${college.city}, ${college.state}"
            tvFees.text = college.fees.replace("/yr", "")
            tvPackage.text = college.avgPackage
            badgeMatch.text = "${college.matchScore}% Match"
            
            // Set Match Badge Color based on score
            val matchColor = if (college.matchScore >= 90) "#3D5AFE" else if (college.matchScore >= 80) "#536DFE" else "#8C9EFF"
            badgeMatch.background.setTint(Color.parseColor(matchColor))

            // Update Save Icon
            updateSaveIcon(this, college)

            btnViewDetails.setOnClickListener { onCollegeClick(college) }
            
            btnSave.setOnClickListener {
                if (SavedCollegeManager.isSaved(root.context, college)) {
                    SavedCollegeManager.removeCollege(root.context, college)
                    college.isSaved = false
                    Toast.makeText(root.context, "Removed from Saved", Toast.LENGTH_SHORT).show()
                } else {
                    SavedCollegeManager.saveCollege(root.context, college)
                    college.isSaved = true
                    Toast.makeText(root.context, "College Saved Successfully", Toast.LENGTH_SHORT).show()
                }
                updateSaveIcon(this, college)
            }
        }
    }
    
    // Helper to update save icon visual state
    private fun updateSaveIcon(binding: ItemAiCollegeBinding, college: College) {
        val isSaved = SavedCollegeManager.isSaved(binding.root.context, college)
        // Note: Using ic_search as placeholder for bookmark if not available, ideally should be bookmark icon
        // Assuming we need to use existing or standard drawable. 
        // Using localized check
        if (isSaved) {
             binding.btnSave.setIconResource(android.R.drawable.star_big_on)
             binding.btnSave.setIconTintResource(R.color.splash_primary)
        } else {
             binding.btnSave.setIconResource(android.R.drawable.star_big_off)
             binding.btnSave.setIconTintResource(android.R.color.darker_gray)
        }
    }

    override fun getItemCount() = colleges.size
}
