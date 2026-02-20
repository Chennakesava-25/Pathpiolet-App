package com.simats.pathpiolet.ui

import android.view.LayoutInflater
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.simats.pathpiolet.databinding.ActivityRoadmapBinding

@Composable
fun RoadmapScreen(
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val binding = ActivityRoadmapBinding.inflate(LayoutInflater.from(context))
            // The back button in the XML - since this is a tab, we probably don't want it to 'finish'
            binding.btnBack.setOnClickListener {
                // Optional: navigate back to Home tab if needed
            }
            binding.root
        }
    )
}
