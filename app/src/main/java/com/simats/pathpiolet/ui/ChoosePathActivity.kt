package com.simats.pathpiolet.ui

import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.simats.pathpiolet.databinding.ActivityChoosePathBinding

class ChoosePathActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChoosePathBinding
    private var selectedPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChoosePathBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        binding.rgPath.setOnCheckedChangeListener { group, checkedId ->
            binding.btnContinue.isEnabled = true
            val radioButton = group.findViewById<RadioButton>(checkedId)
            selectedPath = if (checkedId == binding.rbBtech.id) "B.Tech" else "B.Sc"
        }
        
        // Manual click handling for card views to trigger radio buttons
        binding.rbBtech.setOnClickListener { 
             binding.rgPath.check(binding.rbBtech.id)
        }
        binding.rbBsc.setOnClickListener {
             binding.rgPath.check(binding.rbBsc.id)
        }

        binding.btnContinue.setOnClickListener {
            val intent = Intent(this, PreferencesActivity::class.java)
            intent.putExtra("path", selectedPath)
            startActivity(intent)
        }
    }
}
