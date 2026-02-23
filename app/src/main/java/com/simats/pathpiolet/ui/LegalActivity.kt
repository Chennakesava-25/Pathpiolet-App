package com.simats.pathpiolet.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import com.simats.pathpiolet.databinding.ActivityLegalContentBinding

class LegalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLegalContentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityLegalContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("title") ?: "Information"
        val content = intent.getStringExtra("content") ?: ""

        binding.tvTitle.text = title
        binding.tvContent.text = content

        binding.btnBack.setOnClickListener { finish() }
    }
}
