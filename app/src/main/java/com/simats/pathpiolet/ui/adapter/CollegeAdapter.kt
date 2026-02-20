package com.simats.pathpiolet.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simats.pathpiolet.data.College
import com.simats.pathpiolet.databinding.ItemCollegeBinding

class CollegeAdapter(
    private var colleges: List<College>,
    private val onCollegeClick: (College) -> Unit
) : RecyclerView.Adapter<CollegeAdapter.CollegeViewHolder>() {

    private var filteredColleges: List<College> = colleges

    inner class CollegeViewHolder(private val binding: ItemCollegeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(college: College) {
            binding.tvRankBadge.text = "#${college.rank}"
            binding.tvCollegeName.text = college.name
            binding.tvLocation.text = "${college.city}, ${college.state}"
            binding.tvScore.text = "Score: ${college.score}"
            
            // Set OnClickListener
            binding.btnViewDetails.setOnClickListener {
                onCollegeClick(college)
            }
            binding.root.setOnClickListener {
                 onCollegeClick(college)
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
                it.name.lowercase().contains(lowercaseQuery) ||
                it.city.lowercase().contains(lowercaseQuery) ||
                it.state.lowercase().contains(lowercaseQuery)
            }
        }
        notifyDataSetChanged()
    }
}
