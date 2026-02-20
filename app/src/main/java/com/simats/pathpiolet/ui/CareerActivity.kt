package com.simats.pathpiolet.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.view.Gravity

class CareerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textView = TextView(this)
        textView.text = "Career Success Activity\nComing Soon"
        textView.gravity = Gravity.CENTER
        textView.textSize = 24f
        setContentView(textView)
    }
}
