package com.simats.pathpiolet.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import com.simats.pathpiolet.databinding.ActivityEditProfileBinding
import com.simats.pathpiolet.databinding.ItemEditFieldBinding
import com.simats.pathpiolet.utils.SessionManager
import com.simats.pathpiolet.api.RetrofitClient
import com.simats.pathpiolet.api.UpdateProfileRequest
import com.simats.pathpiolet.api.AuthResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)

        setupFields()
        setupButtons()
    }

    private fun setupFields() {
        ItemEditFieldBinding.bind(binding.fieldFullName.root).apply {
            tvLabel.text = "Full Name"
            etInput.hint = "Enter your full name"
            etInput.setText(sessionManager.getUsername())
        }

        ItemEditFieldBinding.bind(binding.fieldPhone.root).apply {
            tvLabel.text = "Phone Number"
            etInput.hint = "Enter your phone number"
            etInput.setText(sessionManager.getPhone())
        }

        ItemEditFieldBinding.bind(binding.fieldAge.root).apply {
            tvLabel.text = "Age"
            etInput.hint = "Enter your age"
            etInput.setText(sessionManager.getAge().toString())
        }

        ItemEditFieldBinding.bind(binding.fieldEmail.root).apply {
            tvLabel.text = "Email (cannot be changed)"
            etInput.setText(sessionManager.getEmail())
            etInput.isEnabled = false
            etInput.setTextColor(android.graphics.Color.GRAY)
        }

        ItemEditFieldBinding.bind(binding.fieldEducation.root).apply {
            tvLabel.text = "Education Level"
            etInput.hint = "e.g. 12th MPC, B.Tech"
            etInput.setText(sessionManager.getEducation())
        }

        ItemEditFieldBinding.bind(binding.fieldInterested.root).apply {
            tvLabel.text = "Interested Field"
            etInput.hint = "e.g. Software, Core Engineering"
            etInput.setText(sessionManager.getInterested())
        }
    }

    private fun setupButtons() {
        binding.btnBack.setOnClickListener { finish() }
        binding.btnCancel.setOnClickListener { finish() }
        
        val saveAction = {
            val fullName = ItemEditFieldBinding.bind(binding.fieldFullName.root).etInput.text.toString()
            val phone = ItemEditFieldBinding.bind(binding.fieldPhone.root).etInput.text.toString()
            val ageStr = ItemEditFieldBinding.bind(binding.fieldAge.root).etInput.text.toString()
            val age = ageStr.toIntOrNull() ?: 0
            val education = ItemEditFieldBinding.bind(binding.fieldEducation.root).etInput.text.toString()
            val interested = ItemEditFieldBinding.bind(binding.fieldInterested.root).etInput.text.toString()

            val userId = sessionManager.getUserId()
            if (userId != -1) {
                val request = UpdateProfileRequest(fullName, phone, age, education, interested)
                RetrofitClient.instance.updateProfile(userId, request).enqueue(object : Callback<AuthResponse> {
                    override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                        if (response.isSuccessful) {
                            sessionManager.updateProfile(fullName, phone, age, education, interested)
                            Toast.makeText(this@EditProfileActivity, "Profile Updated", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@EditProfileActivity, "Update failed", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                        Toast.makeText(this@EditProfileActivity, "Network error", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "Session error", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        binding.btnSave.setOnClickListener { saveAction() }
        binding.btnSaveTop.setOnClickListener { saveAction() }
    }
}
