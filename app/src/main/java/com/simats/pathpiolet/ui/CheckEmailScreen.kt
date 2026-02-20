package com.simats.pathpiolet.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.pathpiolet.ui.theme.SplashPrimary
import com.simats.pathpiolet.ui.theme.SplashTagline

@Composable
fun CheckEmailScreen(
    onBackToLogin: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            // Success Icon in Circle
            Surface(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                color = Color(0xFFE8F1FF) // Very light blue
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Success",
                        tint = SplashPrimary,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Title
            Text(
                text = "Check Your Email",
                color = SplashPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Instructions
            Text(
                text = "Reset link sent successfully. Please check\nyour email inbox and follow the\ninstructions to reset your password.",
                color = SplashTagline,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Email Display Box
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFE8F1FF),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Email sent to:", fontSize = 12.sp, color = SplashTagline)
                    Text(
                        text = "ss@gmail.com",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = SplashPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Back to Login Button
            Button(
                onClick = onBackToLogin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SplashPrimary),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Back to Login", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Footer - Resend Link
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text("Didn't receive the email?", color = SplashTagline, fontSize = 14.sp)
                TextButton(onClick = { /* Handle resend */ }) {
                    Text("Resend", color = SplashPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}
