package com.simats.pathpiolet.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.widget.addTextChangedListener
import com.simats.pathpiolet.databinding.ActivityContactUsBinding
import com.simats.pathpiolet.databinding.DialogContactSuccessBinding

class ContactUsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityContactUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        setupForm()
        
        binding.btnSubmitContact.setOnClickListener {
            if (validateForm()) {
                showSuccessModal()
            }
        }
    }

    private fun setupForm() {
        binding.btnSubmitContact.isEnabled = false
        binding.btnSubmitContact.alpha = 0.5f

        val fields = listOf(binding.etContactName, binding.etContactEmail, binding.etContactSubject, binding.etContactMessage)
        fields.forEach { field ->
            field.addTextChangedListener {
                val allFilled = fields.all { it.text.isNotEmpty() }
                binding.btnSubmitContact.isEnabled = allFilled
                binding.btnSubmitContact.alpha = if (allFilled) 1.0f else 0.5f
                
                if (field == binding.etContactMessage) {
                    binding.tvContactCharCount.text = "${field.text.length}/500 characters"
                }
            }
        }
    }

    private fun validateForm(): Boolean {
        val email = binding.etContactEmail.text.toString()
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etContactEmail.error = "Invalid email format"
            return false
        }
        return true
    }

    private fun showSuccessModal() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        
        val dialogBinding = DialogContactSuccessBinding.inflate(LayoutInflater.from(this))
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        
        dialog.show()

        // Auto dismiss after 2 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            if (dialog.isShowing) {
                dialog.dismiss()
                clearFields()
            }
        }, 2000)
    }

    private fun clearFields() {
        binding.etContactName.text.clear()
        binding.etContactEmail.text.clear()
        binding.etContactSubject.text.clear()
        binding.etContactMessage.text.clear()
        setupForm() // Reset button state
    }
}
