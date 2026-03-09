package com.simats.pathpiolet.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.simats.pathpiolet.databinding.ActivityWebDeveloperBinding

class WebDeveloperActivity : BaseActivity() {
    private lateinit var binding: ActivityWebDeveloperBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebDeveloperBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.root.setOnClickListener { finish() }

        // Like Button Logic
        val btnLike: android.widget.ImageView = findViewById(com.simats.pathpiolet.R.id.btnLike) ?: return 
        fun updateLikeIcon() {
            val isLiked = com.simats.pathpiolet.data.SavedItemManager.isLiked(this, "Career", "Web Developer")
            if (isLiked) {
                btnLike.setImageResource(android.R.drawable.btn_star_big_on)
                btnLike.setColorFilter(androidx.core.content.ContextCompat.getColor(this, com.simats.pathpiolet.R.color.splash_primary))
            } else {
                btnLike.setImageResource(android.R.drawable.btn_star_big_off)
                btnLike.setColorFilter(android.graphics.Color.GRAY)
            }
        }
        updateLikeIcon()

        btnLike.setOnClickListener {
            com.simats.pathpiolet.data.SavedItemManager.toggleLike(this, "Career", "Web Developer")
            updateLikeIcon()
        }
    }
}
