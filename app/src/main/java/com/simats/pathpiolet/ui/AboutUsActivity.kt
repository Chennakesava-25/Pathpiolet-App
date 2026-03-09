package com.simats.pathpiolet.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import com.simats.pathpiolet.R
import com.simats.pathpiolet.databinding.ActivityAboutUsBinding
import com.simats.pathpiolet.databinding.ItemAboutOfferBinding

class AboutUsActivity : BaseActivity() {

    private lateinit var binding: ActivityAboutUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.root.setOnClickListener { finish() }

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
            val intent = Intent(this, LegalActivity::class.java)
            intent.putExtra("title", "Terms & Conditions")
            intent.putExtra("content", getString(R.string.terms_and_conditions))
            startActivity(intent)
        }
        binding.btnPrivacy.setOnClickListener {
            val intent = Intent(this, LegalActivity::class.java)
            intent.putExtra("title", "Privacy Policy")
            intent.putExtra("content", getString(R.string.privacy_policy))
            startActivity(intent)
        }
    }
}
