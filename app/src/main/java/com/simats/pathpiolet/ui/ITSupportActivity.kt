package com.simats.pathpiolet.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.simats.pathpiolet.databinding.ActivityItSupportBinding

class ITSupportActivity : BaseActivity() {
    private lateinit var binding: ActivityItSupportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItSupportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.root.setOnClickListener { finish() }
    }
}
