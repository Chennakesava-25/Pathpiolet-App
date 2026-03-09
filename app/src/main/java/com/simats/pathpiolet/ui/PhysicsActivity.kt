package com.simats.pathpiolet.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.simats.pathpiolet.databinding.ActivityPhysicsBinding

class PhysicsActivity : BaseActivity() {

    private lateinit var binding: ActivityPhysicsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhysicsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.root.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(com.simats.pathpiolet.R.anim.slide_in_left, com.simats.pathpiolet.R.anim.slide_out_right)
        }
    }
}
