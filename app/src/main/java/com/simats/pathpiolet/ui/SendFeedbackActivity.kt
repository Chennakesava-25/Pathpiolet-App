package com.simats.pathpiolet.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.widget.addTextChangedListener
import com.simats.pathpiolet.R
import com.simats.pathpiolet.databinding.ActivityFeedbackBinding
import com.simats.pathpiolet.databinding.DialogFeedbackSuccessBinding

class SendFeedbackActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedbackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        setupRating()
        setupSpinner()
        setupMessageCounter()
        
        binding.btnSubmit.setOnClickListener {
            showSuccessDialog()
        }
    }

    private fun setupRating() {
        val stars = listOf(binding.star1, binding.star2, binding.star3, binding.star4, binding.star5)
        stars.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                // Update stars UI
                stars.forEachIndexed { i, star ->
                    if (i <= index) {
                        star.setImageResource(android.R.drawable.btn_star_big_on)
                        star.setColorFilter(android.graphics.Color.parseColor("#FBC02D"))
                    } else {
                        star.setImageResource(android.R.drawable.btn_star_big_off)
                        star.setColorFilter(android.graphics.Color.parseColor("#E0E0E0"))
                    }
                }
            }
        }
    }

    private fun setupSpinner() {
        val types = arrayOf("Bug Report", "Feature Request", "General Feedback", "Account Issue")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFeedbackType.adapter = adapter
    }

    private fun setupMessageCounter() {
        binding.etFeedbackMessage.addTextChangedListener { text ->
            val count = text?.length ?: 0
            binding.tvCharCount.text = "$count/500 characters"
            if (count > 500) {
                binding.tvCharCount.setTextColor(Color.RED)
            } else {
                binding.tvCharCount.setTextColor(Color.parseColor("#C0C0C0"))
            }
        }
    }

    private fun showSuccessDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        
        val dialogBinding = DialogFeedbackSuccessBinding.inflate(LayoutInflater.from(this))
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogBinding.btnOk.setOnClickListener {
            dialog.dismiss()
            finish() // Return to Profile after feedback
        }

        dialog.show()
    }
}
