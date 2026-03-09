package com.simats.pathpiolet.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.pathpiolet.ui.theme.SplashPrimary
import com.simats.pathpiolet.ui.theme.SplashTagline
import com.simats.pathpiolet.api.RetrofitClient
import com.simats.pathpiolet.api.ForgotPasswordRequest
import com.simats.pathpiolet.api.AuthResponse
import android.widget.Toast
import com.simats.pathpiolet.ui.components.StandardBackButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    onBackToLogin: () -> Unit,
    onOtpSent: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }
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
            // Top Bar - Back to Login
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StandardBackButton(onClick = onBackToLogin)
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = "Back to Login",
                    color = SplashPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(60.dp))

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

            Spacer(modifier = Modifier.height(40.dp))

            // Title
            Text(
                text = "Forgot Password?",
                color = SplashPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Subtitle
            Text(
                text = "Enter your registered email to reset your\npassword.",
                color = SplashTagline,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

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
                    focusedBorderColor = SplashPrimary,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    disabledTextColor = Color.Black,
                    errorTextColor = Color.Black
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Send OTP Button
            Button(
                onClick = {
                    if (email.isNotEmpty()) {
                        isLoading = true
                        RetrofitClient.instance.forgotPassword(ForgotPasswordRequest(email)).enqueue(object : Callback<AuthResponse> {
                            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                                isLoading = false
                                if (response.isSuccessful) {
                                    Toast.makeText(context, "OTP sent to $email", Toast.LENGTH_SHORT).show()
                                    onOtpSent(email)
                                } else {
                                    val errorMsg = try {
                                        val errorBody = response.errorBody()?.string()
                                        val jsonObject = com.google.gson.JsonParser().parse(errorBody).asJsonObject
                                        jsonObject.get("error")?.getAsString() ?: jsonObject.get("message")?.getAsString() ?: "Failed to generate OTP"
                                    } catch (e: Exception) {
                                        "Error: ${response.code()}"
                                    }
                                    Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
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
                enabled = !isLoading && email.isNotEmpty(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SplashPrimary),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Send OTP", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Tip Box
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFE8F1FF), // Very light blue
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "💡 Tip: ",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SplashTagline
                    )
                    Text(
                        text = "Make sure to check your spam folder if you\ndon't see the email in your inbox.",
                        fontSize = 12.sp,
                        color = SplashTagline,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}
