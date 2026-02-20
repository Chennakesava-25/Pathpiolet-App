package com.simats.pathpiolet.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import com.simats.pathpiolet.databinding.ActivityRateUsBinding
import com.simats.pathpiolet.databinding.DialogRateSuccessBinding

class RateUsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRateUsBinding
    private var selectedRating = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityRateUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }
        binding.btnMaybeLater.setOnClickListener { finish() }

        setupStarRating()
        
        binding.btnRateNow.setOnClickListener {
            showSuccessModal()
        }
    }

    private fun setupStarRating() {
        val stars = listOf(binding.star1, binding.star2, binding.star3, binding.star4, binding.star5)
        stars.forEachIndexed { index, star ->
            star.setOnClickListener {
                selectedRating = index + 1
                updateStarsUI(stars, selectedRating)
                binding.tvRatingThanks.visibility = View.VISIBLE
                binding.btnRateNow.isEnabled = true
            }
        }
    }

    private fun updateStarsUI(stars: List<ImageView>, rating: Int) {
        stars.forEachIndexed { i, star ->
            if (i < rating) {
                star.setImageResource(android.R.drawable.btn_star_big_on)
                star.setColorFilter(Color.parseColor("#FBC02D"))
            } else {
                star.setImageResource(android.R.drawable.btn_star_big_off)
                star.setColorFilter(Color.parseColor("#E0E0E0"))
            }
        }
    }

    private fun showSuccessModal() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        
        val dialogBinding = DialogRateSuccessBinding.inflate(LayoutInflater.from(this))
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        
        // Background dimming is managed by the system standard dialog behavior
        
        dialog.show()

        // Auto dismiss after 2 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            if (dialog.isShowing) {
                dialog.dismiss()
                resetUI()
            }
        }, 2000)
    }

    private fun resetUI() {
        selectedRating = 0
        val stars = listOf(binding.star1, binding.star2, binding.star3, binding.star4, binding.star5)
        updateStarsUI(stars, 0)
        binding.tvRatingThanks.visibility = View.INVISIBLE
        binding.btnRateNow.isEnabled = false
    }
}
