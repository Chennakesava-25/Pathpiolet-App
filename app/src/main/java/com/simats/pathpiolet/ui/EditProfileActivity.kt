package com.simats.pathpiolet.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import com.simats.pathpiolet.databinding.ActivityEditProfileBinding
import com.simats.pathpiolet.databinding.ItemEditFieldBinding

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupFields()
        setupButtons()
    }

    private fun setupFields() {
        // Using binding to access included layouts
        ItemEditFieldBinding.bind(binding.fieldFullName.root).apply {
            tvLabel.text = "Full Name"
            etInput.hint = "Full Name"
            etInput.setText("Student")
        }

        ItemEditFieldBinding.bind(binding.fieldPhone.root).apply {
            tvLabel.text = "Phone Number"
            etInput.hint = "Phone Number"
        }

        ItemEditFieldBinding.bind(binding.fieldAge.root).apply {
            tvLabel.text = "Age"
            etInput.hint = "Age"
        }

        ItemEditFieldBinding.bind(binding.fieldEmail.root).apply {
            tvLabel.text = "Email (cannot be changed)"
            etInput.setText("student@pathpiolet.com")
            etInput.isEnabled = false
            etInput.setTextColor(android.graphics.Color.GRAY)
        }

        ItemEditFieldBinding.bind(binding.fieldEducation.root).apply {
            tvLabel.text = "Education Level"
            etInput.hint = "Education Level"
        }

        ItemEditFieldBinding.bind(binding.fieldInterested.root).apply {
            tvLabel.text = "Interested Field"
            etInput.hint = "Interested Field"
        }
    }

    private fun setupButtons() {
        binding.btnBack.setOnClickListener { finish() }
        binding.btnCancel.setOnClickListener { finish() }
        
        val saveAction = {
            Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.btnSave.setOnClickListener { saveAction() }
        binding.btnSaveTop.setOnClickListener { saveAction() }
    }
}
