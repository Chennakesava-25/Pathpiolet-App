package com.simats.pathpiolet.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.simats.pathpiolet.databinding.ActivityAiProcessingBinding

class AiProcessingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAiProcessingBinding
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAiProcessingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startProcessing()
    }

    private fun startProcessing() {
        // Step 1: 0s - 1s
        handler.postDelayed({
            binding.progressBar.progress = 30
            binding.step1.setTextColor(getColor(android.R.color.holo_blue_dark))
            binding.step2.setTextColor(getColor(android.R.color.darker_gray))
            binding.step3.setTextColor(getColor(android.R.color.darker_gray))
        }, 0)

        // Step 2: 1s - 2s
        handler.postDelayed({
            binding.progressBar.progress = 60
            binding.step2.setTextColor(getColor(android.R.color.holo_blue_dark))
        }, 1000)

        // Step 3: 2s - 3s
        handler.postDelayed({
            binding.progressBar.progress = 90
            binding.step3.setTextColor(getColor(android.R.color.holo_blue_dark))
        }, 2000)

        // Finish: 3s
        handler.postDelayed({
            binding.progressBar.progress = 100
            val intent = Intent(this, AiResultsActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}
