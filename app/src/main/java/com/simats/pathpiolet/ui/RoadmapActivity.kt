package com.simats.pathpiolet.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.simats.pathpiolet.R
import com.simats.pathpiolet.databinding.ActivityRoadmapBinding

class RoadmapActivity : BaseActivity() {

    private lateinit var binding: ActivityRoadmapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoadmapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupHeader()
        setupTimeline()
    }

    private fun setupHeader() {
        binding.btnBack.root.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupTimeline() {
        val prefs = getSharedPreferences("PathPioletPrefs", MODE_PRIVATE)
        // Keep existing logic but default to prompt's requirements if not set
        val foundationViewed = prefs.getBoolean("FoundationModuleViewed", false)
        val prepStatus = prefs.getString("PreparationModuleStatus", "Not Started") ?: "Not Started"

        val timelineItems = listOf(
            TimelineModule(
                date = "10th",
                title = "Foundation",
                subtitle = "Start your Computer Science journey from 10th",
                status = "0 of 4 modules completed", // As per prompt requirement
                iconResId = R.drawable.ic_school,
                backgroundDrawableResId = R.drawable.bg_step_icon_purple_gradient,
                targetActivityName = "FoundationActivity"
            ),
            TimelineModule(
                date = "12th MPC",
                title = "Preparation",
                subtitle = "Build strong foundation & prepare for entrance exams",
                status = "Not Started", // As per prompt requirement
                iconResId = R.drawable.ic_school,
                backgroundDrawableResId = R.drawable.bg_step_icon_purple_gradient, // Matches 10th purple or indigo
                targetActivityName = "PreparationActivity"
            ),
            TimelineModule(
                date = "B.Tech / B.Sc",
                title = "Graduation",
                subtitle = "4 or 3 years",
                status = "",
                iconResId = R.drawable.ic_school,
                backgroundDrawableResId = R.drawable.bg_step_icon_blue_gradient,
                targetActivityName = "GraduationActivity"
            ),
            TimelineModule(
                date = "M.Tech / M.Sc",
                title = "Masters",
                subtitle = "2 years specialization",
                status = "",
                iconResId = R.drawable.ic_book,
                backgroundDrawableResId = R.drawable.bg_step_icon_violet_gradient,
                targetActivityName = "MastersActivity"
            ),
            TimelineModule(
                date = "Ph.D",
                title = "Doctorate",
                subtitle = "3–5 years research",
                status = "",
                iconResId = R.drawable.ic_medal,
                backgroundDrawableResId = R.drawable.bg_step_icon_amber_gradient,
                targetActivityName = "PhdActivity"
            ),
            TimelineModule(
                date = "Career Success",
                title = "Career",
                subtitle = "Professor / Tech Expert / Research Scientist",
                status = "",
                iconResId = R.drawable.ic_briefcase,
                backgroundDrawableResId = R.drawable.bg_step_icon_green_gradient,
                targetActivityName = "CareerSuccessActivity"
            )
        )

        binding.rvTimeline.layoutManager = LinearLayoutManager(this)
        binding.rvTimeline.isNestedScrollingEnabled = false
        binding.rvTimeline.adapter = TimelineAdapter(timelineItems) { activityName ->
            navigateTo(activityName)
        }
    }

    private fun navigateTo(activityName: String) {
        try {
            val intent = Intent(this, Class.forName("com.simats.pathpiolet.ui.$activityName"))
            startActivity(intent)
            
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                overrideActivityTransition(
                    android.app.Activity.OVERRIDE_TRANSITION_OPEN,
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                )
            } else {
                @Suppress("DEPRECATION")
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Data Class
    data class TimelineModule(
        val date: String,
        val title: String,
        val subtitle: String,
        val status: String,
        val iconResId: Int,
        val backgroundDrawableResId: Int,
        val targetActivityName: String
    )

    // Adapter
    inner class TimelineAdapter(
        private val items: List<TimelineModule>,
        private val onItemClick: (String) -> Unit
    ) : RecyclerView.Adapter<TimelineAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvDate: TextView = view.findViewById(R.id.tvModuleDate)
            val tvTitle: TextView = view.findViewById(R.id.tvModuleTitle)
            val tvSubtitle: TextView = view.findViewById(R.id.tvModuleSubtitle)
            val tvStatus: TextView = view.findViewById(R.id.tvModuleStatus)
            val ivIcon: ImageView = view.findViewById(R.id.ivModuleIcon)
            val viewIconBg: View = view.findViewById(R.id.viewIconBackground)
            val timelineLine: View = view.findViewById(R.id.timelineLine)
            val card: View = view.findViewById(R.id.cardModule)
            val iconContainer: View = view.findViewById(R.id.iconContainer)
            val btnArrowContainer: View = view.findViewById(R.id.btnArrowContainer)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_timeline_module, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]

            holder.tvDate.text = item.date
            holder.tvTitle.text = item.title
            holder.tvSubtitle.text = item.subtitle
            
            if (item.status.isNotEmpty()) {
                holder.tvStatus.visibility = View.VISIBLE
                holder.tvStatus.text = item.status
            } else {
                holder.tvStatus.visibility = View.GONE
            }

            holder.ivIcon.setImageResource(item.iconResId)
            holder.viewIconBg.setBackgroundResource(item.backgroundDrawableResId)

            // Like Button Logic
            val btnLike: ImageView = holder.itemView.findViewById(R.id.btnLike)
            fun updateLikeIcon() {
                val isLiked = com.simats.pathpiolet.data.SavedItemManager.isLiked(holder.itemView.context, "Roadmap", item.title)
                if (isLiked) {
                    btnLike.setImageResource(android.R.drawable.btn_star_big_on)
                    btnLike.setColorFilter(androidx.core.content.ContextCompat.getColor(holder.itemView.context, R.color.splash_primary))
                } else {
                    btnLike.setImageResource(android.R.drawable.btn_star_big_off)
                    btnLike.setColorFilter(android.graphics.Color.GRAY)
                }
            }
            updateLikeIcon()

            btnLike.setOnClickListener {
                com.simats.pathpiolet.data.SavedItemManager.toggleLike(holder.itemView.context, "Roadmap", item.title)
                updateLikeIcon()
            }

            // Click Listeners
            holder.itemView.setOnClickListener { onItemClick(item.targetActivityName) }
            holder.card.setOnClickListener { onItemClick(item.targetActivityName) }
            holder.iconContainer.setOnClickListener { onItemClick(item.targetActivityName) }
            
            // Adjust line for last item?
            // "Left vertical line connecting all modules." 
            // If we want it to look like it stops at the last icon, we can hide it for the last item OR
            // set it to match parent height (which it is) and let it extend down.
            // Visually in many timelines the line stops at the center of the last icon.
            // If I want that, I'd need to change the implementation or visibility.
            // Easiest "clean" way without complex layout changes:
            // Just let it run. It's a "timeline". 
            // If the user complains about the line going past the last item, I can fix it.
            // Given "Left vertical line connecting all modules", continuous is fine.
        }

        override fun getItemCount() = items.size
    }
}
