package com.simats.pathpiolet.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.simats.pathpiolet.databinding.ActivityPreferencesBinding

class PreferencesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreferencesBinding
    private var path: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreferencesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        path = intent.getStringExtra("path")

        binding.btnBack.setOnClickListener { finish() }
        
        binding.sliderBudget.addOnChangeListener { _, value, _ ->
            binding.tvBudget.text = "₹${(value / 1000).toInt()}K"
        }

        binding.btnGetRecommendations.setOnClickListener {
             val intent = Intent(this, AiProcessingActivity::class.java)
             startActivity(intent)
        }
    }
}
