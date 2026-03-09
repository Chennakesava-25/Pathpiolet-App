package com.simats.pathpiolet.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.simats.pathpiolet.api.VerifyOtpRequest
import com.simats.pathpiolet.api.AuthResponse
import android.widget.Toast
import com.simats.pathpiolet.ui.components.StandardBackButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyOtpScreen(
    email: String,
    isFromSignUp: Boolean = false,
    onBack: () -> Unit,
    onOtpVerified: () -> Unit,
    onSignUpSuccess: () -> Unit = {}
) {
    var otp by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val sessionManager = remember { com.simats.pathpiolet.utils.SessionManager(context) }

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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StandardBackButton(onClick = onBack)
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = if (isFromSignUp) "Verify Registration" else "Verify OTP", 
                    color = SplashPrimary, 
                    fontSize = 18.sp, 
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Logo
            Surface(
                modifier = Modifier.size(70.dp),
                shape = RoundedCornerShape(16.dp),
                color = SplashPrimary,
                shadowElevation = 4.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "PP",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Enter OTP",
                color = SplashPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Enter the 6-digit code sent to\n$email",
                color = SplashTagline,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(
                value = otp,
                onValueChange = { if (it.length <= 6) otp = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("6-digit code", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = SplashPrimary,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (otp.length == 6) {
                        isLoading = true
                        val apiCall = if (isFromSignUp) {
                            RetrofitClient.instance.verifySignup(VerifyOtpRequest(email, otp))
                        } else {
                            RetrofitClient.instance.verifyOtp(VerifyOtpRequest(email, otp))
                        }

                        apiCall.enqueue(object : Callback<AuthResponse> {
                            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                                isLoading = false
                                if (response.isSuccessful) {
                                    if (isFromSignUp) {
                                        val authResponse = response.body()
                                        authResponse?.user?.let { user ->
                                            sessionManager.saveUser(
                                                user.id, 
                                                user.username, 
                                                user.email, 
                                                user.phone, 
                                                user.age, 
                                                user.education_level, 
                                                user.interested_field
                                            )
                                        }
                                        Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()
                                        onSignUpSuccess()
                                    } else {
                                        onOtpVerified()
                                    }
                                } else {
                                    val errorMsg = try {
                                        val errorBody = response.errorBody()?.string()
                                        val jsonObject = com.google.gson.JsonParser().parse(errorBody).asJsonObject
                                        jsonObject.get("error")?.getAsString() ?: "Invalid OTP"
                                    } catch (e: Exception) {
                                        "Invalid OTP"
                                    }
                                    Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
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
                enabled = !isLoading && otp.length == 6,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SplashPrimary)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Verify OTP", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
