package com.simats.pathpiolet.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.pathpiolet.ui.theme.SplashPrimary

@Composable
fun CollegesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F7FF))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Colleges",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = SplashPrimary
        )
        Text(
            text = "Coming Soon: Explore Top CS Colleges",
            color = Color.Gray,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
