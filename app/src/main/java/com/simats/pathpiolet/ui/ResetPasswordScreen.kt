package com.simats.pathpiolet.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.pathpiolet.ui.theme.SplashPrimary
import com.simats.pathpiolet.ui.theme.SplashTagline
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.simats.pathpiolet.api.RetrofitClient
import com.simats.pathpiolet.api.ResetPasswordRequest
import com.simats.pathpiolet.api.AuthResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(
    email: String,
    onPasswordReset: () -> Unit
) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

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

            Text(
                text = "New Password",
                color = SplashPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Set a new password for your account\n$email",
                color = SplashTagline,
                fontSize = 14.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("New Password") },
                leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null, tint = SplashTagline) },
                shape = RoundedCornerShape(16.dp),
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = SplashPrimary,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Confirm New Password") },
                leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null, tint = SplashTagline) },
                shape = RoundedCornerShape(16.dp),
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = SplashPrimary,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (password.isBlank()) {
                        Toast.makeText(context, "Please enter a new password", Toast.LENGTH_SHORT).show()
                    } else if (password.length < 8) {
                        Toast.makeText(context, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
                    } else if (!password.any { it.isUpperCase() }) {
                        Toast.makeText(context, "Password must contain at least one uppercase letter", Toast.LENGTH_SHORT).show()
                    } else if (!password.any { it.isDigit() }) {
                        Toast.makeText(context, "Password must contain at least one number", Toast.LENGTH_SHORT).show()
                    } else if (password != confirmPassword) {
                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    } else {
                        isLoading = true
                        RetrofitClient.instance.resetPassword(ResetPasswordRequest(email, password)).enqueue(object : Callback<AuthResponse> {
                            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                                isLoading = false
                                if (response.isSuccessful) {
                                    Toast.makeText(context, "Password reset successful", Toast.LENGTH_SHORT).show()
                                    onPasswordReset()
                                } else {
                                    Toast.makeText(context, response.body()?.error ?: "Reset failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                                isLoading = false
                                Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isLoading && password.isNotEmpty() && confirmPassword.isNotEmpty(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SplashPrimary)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Reset Password", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
