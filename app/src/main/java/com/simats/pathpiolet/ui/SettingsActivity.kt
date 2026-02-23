package com.simats.pathpiolet.ui

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import com.simats.pathpiolet.MainActivity
import com.simats.pathpiolet.databinding.ActivitySettingsBinding
import com.simats.pathpiolet.databinding.DialogDeleteAccountConfirmBinding
import com.simats.pathpiolet.databinding.DialogLogoutConfirmBinding
import com.simats.pathpiolet.databinding.DialogPrivacyDetailsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val PREFS_NAME = "pathpiolet_prefs"
    private val KEY_THEME = "app_theme"

    override fun onCreate(savedInstanceState: Bundle?) {
        // Force Light Mode for Settings
        delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
        
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        setupPreferences()
        setupSecurity()
        setupLogout()
        setupDeleteAccount()
    }

    private fun setupPreferences() {
        // Theme and Password settings removed per request
    }

    private fun setupSecurity() {
        binding.btnAccountPrivacy.setOnClickListener {
            showPrivacyDetailsDialog()
        }
    }

    private fun showPrivacyDetailsDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding = DialogPrivacyDetailsBinding.inflate(LayoutInflater.from(this))
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogBinding.btnClose.setOnClickListener { dialog.dismiss() }
        dialogBinding.btnOk.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    private fun setupLogout() {
        binding.btnLogout.setOnClickListener {
            showLogoutConfirmation()
        }
    }

    private fun showLogoutConfirmation() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding = DialogLogoutConfirmBinding.inflate(LayoutInflater.from(this))
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogBinding.btnCancel.setOnClickListener { dialog.dismiss() }
        dialogBinding.btnLogoutConfirm.setOnClickListener {
            dialog.dismiss()
            performExit()
        }

        dialog.show()
    }

    private fun setupDeleteAccount() {
        binding.btnDeleteAccount.setOnClickListener {
            showDeleteAccountConfirmation()
        }
    }

    private fun showDeleteAccountConfirmation() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding = DialogDeleteAccountConfirmBinding.inflate(LayoutInflater.from(this))
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogBinding.btnCancel.setOnClickListener { dialog.dismiss() }
        dialogBinding.btnDeleteConfirm.setOnClickListener {
            dialog.dismiss()
            performExit()
        }

        dialog.show()
    }

    private fun performExit() {
        // Clear session logic here
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
