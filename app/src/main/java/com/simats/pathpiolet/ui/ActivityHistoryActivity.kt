package com.simats.pathpiolet.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.simats.pathpiolet.R
import com.simats.pathpiolet.databinding.ActivityHistoryBinding
import com.simats.pathpiolet.databinding.ItemActivityHistoryBinding

class ActivityHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private val allItems = listOf(
        HistoryItem("IIT Bombay", "Computer Science & Engineering", "1 hour ago", "College"),
        HistoryItem("B.Tech CS Roadmap", "Viewed complete pathway", "2 hours ago", "Roadmap"),
        HistoryItem("JEE Main 2026", "Added to calendar", "19 hours ago", "Calendar"),
        HistoryItem("IIT Delhi", "Artificial Intelligence", "21 hours ago", "College"),
        HistoryItem("M.Tech in AI & ML", "Course details viewed", "2 days ago", "Course"),
        HistoryItem("After 12th Options", "Explored pathways", "2 days ago", "Roadmap"),
        HistoryItem("IIIT Hyderabad", "Computer Science", "2 days ago", "College"),
        HistoryItem("BITSAT 2026", "Added to calendar", "3 days ago", "Calendar")
    )

    private lateinit var adapter: ActivityHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        setupRecyclerView()
        setupTabs()
    }

    private fun setupRecyclerView() {
        adapter = ActivityHistoryAdapter()
        binding.rvActivityHistory.layoutManager = LinearLayoutManager(this)
        binding.rvActivityHistory.adapter = adapter
        adapter.updateList(allItems)
    }

    private fun setupTabs() {
        val tabs = listOf(binding.tabAll, binding.tabColleges, binding.tabRoadmaps, binding.tabCalendar)
        
        binding.tabAll.setOnClickListener { 
            updateTabsUI(binding.tabAll)
            adapter.updateList(allItems) 
        }
        binding.tabColleges.setOnClickListener { 
            updateTabsUI(binding.tabColleges)
            adapter.updateList(allItems.filter { it.type == "College" }) 
        }
        binding.tabRoadmaps.setOnClickListener { 
            updateTabsUI(binding.tabRoadmaps)
            adapter.updateList(allItems.filter { it.type == "Roadmap" }) 
        }
        binding.tabCalendar.setOnClickListener { 
            updateTabsUI(binding.tabCalendar)
            adapter.updateList(allItems.filter { it.type == "Calendar" }) 
        }
    }

    private fun updateTabsUI(selectedTab: androidx.cardview.widget.CardView) {
        val tabs = listOf(binding.tabAll, binding.tabColleges, binding.tabRoadmaps, binding.tabCalendar)
        tabs.forEach { tab ->
            if (tab == selectedTab) {
                tab.setCardBackgroundColor(ContextCompat.getColor(this, R.color.purple_700)) // Use primary blue
                (tab.getChildAt(0) as android.widget.TextView).setTextColor(android.graphics.Color.WHITE)
            } else {
                tab.setCardBackgroundColor(android.graphics.Color.parseColor("#F5F6FA"))
                (tab.getChildAt(0) as android.widget.TextView).setTextColor(android.graphics.Color.parseColor("#7C86A2"))
            }
        }
    }

    data class HistoryItem(val title: String, val subtitle: String, val time: String, val type: String)

    class ActivityHistoryAdapter : RecyclerView.Adapter<ActivityHistoryAdapter.ViewHolder>() {
        private var items: List<HistoryItem> = emptyList()

        class ViewHolder(val binding: ItemActivityHistoryBinding) : RecyclerView.ViewHolder(binding.root)

        fun updateList(newList: List<HistoryItem>) {
            items = newList
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemActivityHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.binding.tvTitle.text = item.title
            holder.binding.tvSubtitle.text = item.subtitle
            holder.binding.tvTime.text = item.time
            holder.binding.tvTag.text = item.type
            
            // Set icon based on type
            val iconRes = when(item.type) {
                "College" -> android.graphics.drawable.Icon.createWithResource(holder.itemView.context, android.R.drawable.ic_menu_myplaces)
                "Roadmap" -> android.graphics.drawable.Icon.createWithResource(holder.itemView.context, android.R.drawable.ic_menu_directions)
                "Calendar" -> android.graphics.drawable.Icon.createWithResource(holder.itemView.context, android.R.drawable.ic_menu_today)
                else -> android.graphics.drawable.Icon.createWithResource(holder.itemView.context, android.R.drawable.ic_menu_agenda)
            }
            holder.binding.ivIcon.setImageIcon(iconRes)
        }

        override fun getItemCount(): Int = items.size
    }
}
