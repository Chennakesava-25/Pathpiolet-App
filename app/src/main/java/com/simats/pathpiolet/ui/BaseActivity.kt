package com.simats.pathpiolet.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
    }

    override fun setContentView(view: android.view.View?) {
        super.setContentView(view)
        view?.let { applyEdgeToEdge(it) }
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        val rootView = findViewById<android.view.View>(android.R.id.content)
        rootView?.let { applyEdgeToEdge(it) }
    }

    private fun applyEdgeToEdge(view: android.view.View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(top = systemBars.top)
            insets
        }
        
        // Enforce light status bar icons (black icons for white backgrounds)
        val window = window
        val decorView = window.decorView
        androidx.core.view.WindowInsetsControllerCompat(window, decorView).isAppearanceLightStatusBars = true
    }
}
