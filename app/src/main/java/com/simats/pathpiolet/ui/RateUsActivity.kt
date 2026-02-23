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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import com.simats.pathpiolet.databinding.ActivityRateUsBinding
import com.simats.pathpiolet.databinding.DialogRateSuccessBinding
import com.simats.pathpiolet.api.RetrofitClient
import com.simats.pathpiolet.api.RatingRequest
import com.simats.pathpiolet.api.AuthResponse
import com.simats.pathpiolet.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
            submitRating()
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

    private fun submitRating() {
        val sessionManager = SessionManager(this)
        val userId = sessionManager.getUserId()
        if (userId == -1) {
            Toast.makeText(this, "Session expired, please login again", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedRating == 0) {
            Toast.makeText(this, "Please select a rating", Toast.LENGTH_SHORT).show()
            return
        }

        binding.btnRateNow.isEnabled = false
        binding.btnRateNow.text = "Sending..."

        val request = RatingRequest(userId, selectedRating)
        RetrofitClient.instance.rateApp(request).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                binding.btnRateNow.isEnabled = true
                binding.btnRateNow.text = "Rate Now"
                if (response.isSuccessful) {
                    showSuccessModal()
                } else {
                    Toast.makeText(this@RateUsActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                binding.btnRateNow.isEnabled = true
                binding.btnRateNow.text = "Rate Now"
                Toast.makeText(this@RateUsActivity, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showSuccessModal() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        
        val dialogBinding = DialogRateSuccessBinding.inflate(LayoutInflater.from(this))
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        
        dialog.show()

        // Auto dismiss after 2 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            if (dialog.isShowing) {
                dialog.dismiss()
                finish() // Return after rating
            }
        }, 2000)
    }
}
