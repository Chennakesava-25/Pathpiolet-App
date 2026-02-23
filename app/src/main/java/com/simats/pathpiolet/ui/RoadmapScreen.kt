package com.simats.pathpiolet.ui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.simats.pathpiolet.R
import com.simats.pathpiolet.databinding.ActivityRoadmapBinding

@Composable
fun RoadmapScreen(
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val binding = ActivityRoadmapBinding.inflate(LayoutInflater.from(context))
            
            // Setup Header (Back button logic if needed, currently just closes activity if it was an activity)
            binding.btnBack.setOnClickListener {
                // Since this is a fragment/composable in MainActivity, distinct back behavior might be needed.
                // For now, leaving empty or could handle specific navigation.
            }

            // Setup Timeline
            setupTimeline(context, binding)

            binding.root
        }
    )
}

private fun setupTimeline(context: Context, binding: ActivityRoadmapBinding) {
    val prefs = context.getSharedPreferences("PathPioletPrefs", Context.MODE_PRIVATE)
    val foundationViewed = prefs.getBoolean("FoundationModuleViewed", false)
    val prepStatus = prefs.getString("PreparationModuleStatus", "Not Started") ?: "Not Started"

    val timelineItems = listOf(
        TimelineModuleData(
            date = "10th",
            title = "Foundation",
            subtitle = "Start your Computer Science journey from 10th",
            status = "0 of 4 modules completed",
            iconResId = R.drawable.ic_school,
            backgroundDrawableResId = R.drawable.bg_step_icon_purple_gradient,
            targetActivityName = "FoundationActivity"
        ),
        TimelineModuleData(
            date = "12th MPC",
            title = "Preparation",
            subtitle = "Build strong foundation & prepare for entrance exams",
            status = "Not Started", 
            iconResId = R.drawable.ic_school,
            backgroundDrawableResId = R.drawable.bg_step_icon_purple_gradient, 
            targetActivityName = "PreparationActivity"
        ),
        TimelineModuleData(
            date = "B.Tech / B.Sc",
            title = "Graduation",
            subtitle = "4 or 3 years",
            status = "",
            iconResId = R.drawable.ic_school,
            backgroundDrawableResId = R.drawable.bg_step_icon_blue_gradient,
            targetActivityName = "GraduationActivity"
        ),
        TimelineModuleData(
            date = "M.Tech / M.Sc",
            title = "Masters",
            subtitle = "2 years specialization",
            status = "",
            iconResId = R.drawable.ic_book,
            backgroundDrawableResId = R.drawable.bg_step_icon_violet_gradient,
            targetActivityName = "MastersActivity"
        ),
        TimelineModuleData(
            date = "Ph.D",
            title = "Doctorate",
            subtitle = "3–5 years research",
            status = "",
            iconResId = R.drawable.ic_medal,
            backgroundDrawableResId = R.drawable.bg_step_icon_amber_gradient,
            targetActivityName = "PhdActivity"
        ),
        TimelineModuleData(
            date = "Career Success",
            title = "Career",
            subtitle = "Professor / Tech Expert / Research Scientist",
            status = "",
            iconResId = R.drawable.ic_briefcase,
            backgroundDrawableResId = R.drawable.bg_step_icon_green_gradient,
            targetActivityName = "CareerSuccessActivity"
        )
    )

    binding.rvTimeline.layoutManager = LinearLayoutManager(context)
    binding.rvTimeline.adapter = TimelineScreenAdapter(timelineItems) { activityName ->
        navigateTo(context, activityName)
    }
}

private fun navigateTo(context: Context, activityName: String) {
    try {
        val intent = Intent(context, Class.forName("com.simats.pathpiolet.ui.$activityName"))
        context.startActivity(intent)
        // Transitions might need Activity context, can try casting if needed, or skip for Composable context
        // if (context is Activity) { ... }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

// Data Class (Renamed to avoid conflict if in same package, though file-private or separate is better)
data class TimelineModuleData(
    val date: String,
    val title: String,
    val subtitle: String,
    val status: String,
    val iconResId: Int,
    val backgroundDrawableResId: Int,
    val targetActivityName: String
)

// Adapter
class TimelineScreenAdapter(
    private val items: List<TimelineModuleData>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<TimelineScreenAdapter.ViewHolder>() {

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

        // Click Listeners
        holder.itemView.setOnClickListener { onItemClick(item.targetActivityName) }
        holder.card.setOnClickListener { onItemClick(item.targetActivityName) }
        holder.iconContainer.setOnClickListener { onItemClick(item.targetActivityName) }
    }

    override fun getItemCount() = items.size
}
