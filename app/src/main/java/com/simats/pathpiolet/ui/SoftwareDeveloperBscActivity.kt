package com.simats.pathpiolet.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.simats.pathpiolet.databinding.ActivitySoftwareDeveloperBscBinding

class SoftwareDeveloperBscActivity : BaseActivity() {
    private lateinit var binding: ActivitySoftwareDeveloperBscBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySoftwareDeveloperBscBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.root.setOnClickListener { finish() }
    }
}
