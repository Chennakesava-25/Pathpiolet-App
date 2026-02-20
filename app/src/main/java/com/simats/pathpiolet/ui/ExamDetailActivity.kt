package com.simats.pathpiolet.ui

import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ExamDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textView = TextView(this)
        textView.text = "Exam Detail\nComing Soon"
        textView.gravity = Gravity.CENTER
        textView.textSize = 24f
        setContentView(textView)
        
        // Setup back button logic
    }
}
