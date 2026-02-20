package com.simats.pathpiolet.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.pathpiolet.ui.theme.SplashBackground
import com.simats.pathpiolet.ui.theme.SplashPrimary
import com.simats.pathpiolet.ui.theme.SplashTagline

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SplashBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo - Rounded Square Box with PP
            Surface(
                modifier = Modifier
                    .size(100.dp),
                shape = RoundedCornerShape(16.dp),
                color = SplashPrimary,
                shadowElevation = 8.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "PP",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // App Name
            Text(
                text = "PathPoilet",
                color = SplashPrimary,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tagline
            Text(
                text = "Plan Your Computer Science Journey\nfrom 10th to PhD",
                color = SplashTagline,
                fontSize = 16.sp,
                lineHeight = 22.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 40.dp)
            )
        }
    }
}
