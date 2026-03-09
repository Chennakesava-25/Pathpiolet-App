package com.simats.pathpiolet.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.simats.pathpiolet.databinding.ActivityWebDeveloperBscBinding

class WebDeveloperBscActivity : BaseActivity() {
    private lateinit var binding: ActivityWebDeveloperBscBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebDeveloperBscBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.root.setOnClickListener { finish() }
    }
}
