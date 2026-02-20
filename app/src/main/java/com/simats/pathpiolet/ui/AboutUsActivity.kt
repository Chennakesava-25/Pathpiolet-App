package com.simats.pathpiolet.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import com.simats.pathpiolet.databinding.ActivityAboutUsBinding
import com.simats.pathpiolet.databinding.ItemAboutOfferBinding

class AboutUsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        setupOfferings()
        setupLegalLinks()
    }

    private fun setupOfferings() {
        val offers = listOf(
            "Personalized Career Roadmaps",
            "College Discovery & Comparison",
            "Educational Pathway Guidance",
            "Exam Calendar & Reminders",
            "Expert Career Advice"
        )
        val bindings = listOf(binding.offer1, binding.offer2, binding.offer3, binding.offer4, binding.offer5)
        
        bindings.forEachIndexed { index, offerInclude ->
            ItemAboutOfferBinding.bind(offerInclude.root).tvOfferText.text = offers[index]
        }
    }

    private fun setupLegalLinks() {
        binding.btnTerms.setOnClickListener {
            Toast.makeText(this, "Opening Terms & Conditions...", Toast.LENGTH_SHORT).show()
            // In a real app, you would launch an Activity with a WebView
        }
        binding.btnPrivacy.setOnClickListener {
            Toast.makeText(this, "Opening Privacy Policy...", Toast.LENGTH_SHORT).show()
        }
    }
}
