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
        
        fetchActivity()
    }

    private fun fetchActivity() {
        val userId = com.simats.pathpiolet.utils.SessionManager(this).getUserId()
        if (userId == -1) return

        com.simats.pathpiolet.api.RetrofitClient.instance.getActivityHistory(userId)
            .enqueue(object : retrofit2.Callback<List<com.simats.pathpiolet.api.ActivityItem>> {
                override fun onResponse(
                    call: retrofit2.Call<List<com.simats.pathpiolet.api.ActivityItem>>,
                    response: retrofit2.Response<List<com.simats.pathpiolet.api.ActivityItem>>
                ) {
                    if (response.isSuccessful) {
                        val apiItems = response.body() ?: emptyList()
                        val historyItems = apiItems.map { 
                            HistoryItem(it.title, it.subtitle, it.time, it.type)
                        }
                        adapter.updateList(historyItems)
                    }
                }

                override fun onFailure(call: retrofit2.Call<List<com.simats.pathpiolet.api.ActivityItem>>, t: Throwable) {
                    android.widget.Toast.makeText(this@ActivityHistoryActivity, "Failed to load activity", android.widget.Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun setupTabs() {
        binding.tabAll.setOnClickListener { 
            updateTabsUI(binding.tabAll)
            fetchActivity() // Re-fetch or filter local list? Let's filter local list for better UX
        }
        binding.tabColleges.setOnClickListener { 
            updateTabsUI(binding.tabColleges)
            // Filtering will be handled in adapter or via a property
            adapter.filterByType("College")
        }
        binding.tabRoadmaps.setOnClickListener { 
            updateTabsUI(binding.tabRoadmaps)
            // Roadmap items are colleges in this context (as per user request)
            adapter.filterByType("College") 
        }
        binding.tabCalendar.setOnClickListener { 
            updateTabsUI(binding.tabCalendar)
            adapter.filterByType("Calendar") 
        }
    }

    private fun updateTabsUI(selectedTab: androidx.cardview.widget.CardView) {
        val tabs = listOf(binding.tabAll, binding.tabColleges, binding.tabRoadmaps, binding.tabCalendar)
        tabs.forEach { tab ->
            if (tab == selectedTab) {
                tab.setCardBackgroundColor(ContextCompat.getColor(this, R.color.purple_700))
                (tab.getChildAt(0) as android.widget.TextView).setTextColor(android.graphics.Color.WHITE)
            } else {
                tab.setCardBackgroundColor(ContextCompat.getColor(this, R.color.app_input_bg))
                (tab.getChildAt(0) as android.widget.TextView).setTextColor(ContextCompat.getColor(this, R.color.app_text_primary))
            }
        }
    }

    data class HistoryItem(val title: String, val subtitle: String, val time: String, val type: String)

    class ActivityHistoryAdapter : RecyclerView.Adapter<ActivityHistoryAdapter.ViewHolder>() {
        private var items: List<HistoryItem> = emptyList()
        private var fullList: List<HistoryItem> = emptyList()

        class ViewHolder(val binding: ItemActivityHistoryBinding) : RecyclerView.ViewHolder(binding.root)

        fun updateList(newList: List<HistoryItem>) {
            fullList = newList
            items = newList
            notifyDataSetChanged()
        }

        fun filterByType(type: String?) {
            items = if (type == null) fullList else fullList.filter { it.type == type }
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
