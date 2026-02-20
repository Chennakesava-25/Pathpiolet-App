package com.simats.pathpiolet.ui

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import com.simats.pathpiolet.databinding.ActivityAskQuestionBinding
import com.simats.pathpiolet.databinding.DialogQuestionPostedBinding

class AskQuestionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAskQuestionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityAskQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }
        binding.btnCancel.setOnClickListener { finish() }
        
        binding.btnPost.setOnClickListener {
            showSuccessDialog()
        }
    }

    private fun showSuccessDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        
        val dialogBinding = DialogQuestionPostedBinding.inflate(LayoutInflater.from(this))
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogBinding.btnGoToQna.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, QnaActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        dialog.show()
    }
}
