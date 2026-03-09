package com.simats.pathpiolet.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simats.pathpiolet.data.College
import com.simats.pathpiolet.databinding.ItemCollegeBinding

import com.simats.pathpiolet.data.SavedItemManager
import android.widget.Toast

class CollegeAdapter(
    private var colleges: List<College>,
    private val onCollegeClick: (College) -> Unit
) : RecyclerView.Adapter<CollegeAdapter.CollegeViewHolder>() {

    private var filteredColleges: List<College> = colleges

    inner class CollegeViewHolder(private val binding: ItemCollegeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(college: College) {
            if (college.nirfRank > 0) {
                binding.tvRankBadge.visibility = android.view.View.VISIBLE
                binding.tvRankBadge.text = "#${college.nirfRank}"
            } else {
                binding.tvRankBadge.visibility = android.view.View.GONE
            }
            binding.tvCollegeName.text = college.name ?: "N/A"
            binding.tvLocation.text = "${college.city ?: "N/A"}, ${college.state ?: ""}"
            if (college.score > 0) {
                binding.tvScore.visibility = android.view.View.VISIBLE
                binding.tvScore.text = "Score: ${college.score}"
            } else {
                binding.tvScore.visibility = android.view.View.GONE
            }
            
            // Like Button Logic
            updateLikeIcon(college)
            binding.btnLike.setOnClickListener {
                val context = binding.root.context
                val userId = com.simats.pathpiolet.utils.SessionManager(context).getUserId()
                if (SavedItemManager.isSaved(context, college)) {
                    SavedItemManager.removeCollege(context, userId, college)
                    college.isSaved = false
                    Toast.makeText(context, "Removed from Saved", Toast.LENGTH_SHORT).show()
                } else {
                    SavedItemManager.saveCollege(context, userId, college)
                    college.isSaved = true
                    Toast.makeText(context, "College Saved", Toast.LENGTH_SHORT).show()
                }
                updateLikeIcon(college)
            }

            // Set OnClickListener
            binding.btnViewDetails.setOnClickListener {
                onCollegeClick(college)
            }
            binding.root.setOnClickListener {
                 onCollegeClick(college)
            }
        }

        private fun updateLikeIcon(college: College) {
            val isSaved = SavedItemManager.isSaved(binding.root.context, college)
            if (isSaved) {
                binding.btnLike.setImageResource(android.R.drawable.btn_star_big_on)
                binding.btnLike.setColorFilter(androidx.core.content.ContextCompat.getColor(binding.root.context, com.simats.pathpiolet.R.color.splash_primary))
            } else {
                binding.btnLike.setImageResource(android.R.drawable.btn_star_big_off)
                binding.btnLike.setColorFilter(android.graphics.Color.GRAY)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollegeViewHolder {
        val binding = ItemCollegeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CollegeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CollegeViewHolder, position: Int) {
        holder.bind(filteredColleges[position])
    }

    override fun getItemCount(): Int = filteredColleges.size

    fun filter(query: String) {
        filteredColleges = if (query.isEmpty()) {
            colleges
        } else {
            val lowercaseQuery = query.lowercase()
            colleges.filter {
                (it.name?.lowercase()?.contains(lowercaseQuery) == true) ||
                (it.city?.lowercase()?.contains(lowercaseQuery) == true) ||
                (it.state?.lowercase()?.contains(lowercaseQuery) == true)
            }
        }
        notifyDataSetChanged()
    }
}
