package com.simats.pathpiolet.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.pathpiolet.ui.theme.SplashPrimary
import com.simats.pathpiolet.ui.theme.SplashTagline

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onSignUpClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

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
            Spacer(modifier = Modifier.height(40.dp))

            // Logo
            Surface(
                modifier = Modifier.size(80.dp),
                shape = RoundedCornerShape(16.dp),
                color = SplashPrimary,
                shadowElevation = 4.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "PP",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Title
            Text(
                text = "Welcome Back",
                color = SplashPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "Login to continue your CS journey",
                color = SplashTagline,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Email address") },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Email, contentDescription = null, tint = SplashTagline)
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = SplashPrimary
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Password") },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Lock, contentDescription = null, tint = SplashTagline)
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        // Using a simple text for eye icon since I don't want to import more icons yet
                        Text(if (passwordVisible) "👁️" else "👁️‍🗨️")
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = SplashPrimary
                ),
                singleLine = true
            )

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                TextButton(onClick = onForgotPasswordClick) {
                    Text("Forgot Password?", color = SplashPrimary, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Login Button
            Button(
                onClick = onLoginSuccess,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SplashPrimary),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text("Login", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // OR Divider
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE0E0E0))
                Text(
                    text = "OR",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = SplashTagline,
                    fontSize = 12.sp
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE0E0E0))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Google Login
            OutlinedButton(
                onClick = { /* Handle Google login */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE0E0E0))),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("G", color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 20.sp) // Mock Google Icon
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Continue with Google", color = SplashTagline, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Footer
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text("Don't have an account?", color = SplashTagline, fontSize = 14.sp)
                TextButton(onClick = onSignUpClick) {
                    Text("Sign Up", color = SplashPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}
