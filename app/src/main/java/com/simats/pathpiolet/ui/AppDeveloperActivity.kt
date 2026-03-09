package com.simats.pathpiolet.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.simats.pathpiolet.databinding.ActivityAppDeveloperBinding

class AppDeveloperActivity : BaseActivity() {
    private lateinit var binding: ActivityAppDeveloperBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppDeveloperBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.root.setOnClickListener { finish() }
    }
}
