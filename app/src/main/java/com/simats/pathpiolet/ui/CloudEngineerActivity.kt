package com.simats.pathpiolet.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.simats.pathpiolet.databinding.ActivityCloudEngineerBinding

class CloudEngineerActivity : BaseActivity() {
    private lateinit var binding: ActivityCloudEngineerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCloudEngineerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.root.setOnClickListener { finish() }
    }
}
