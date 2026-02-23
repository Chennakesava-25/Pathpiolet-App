package com.simats.pathpiolet.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.pathpiolet.ui.theme.SplashPrimary
import com.simats.pathpiolet.ui.theme.SplashTagline
import com.simats.pathpiolet.api.*
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.simats.pathpiolet.utils.SessionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onLoginClick: () -> Unit,
    onSignUpSuccess: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    val handleSignUp = {
        if (fullName.isBlank() || phoneNumber.isBlank() || age.isBlank() || email.isBlank() || password.isBlank()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
        } else if (!email.endsWith("@gmail.com")) {
            Toast.makeText(context, "Only @gmail.com addresses are allowed", Toast.LENGTH_SHORT).show()
        } else if (phoneNumber.length != 10 || !phoneNumber.all { it.isDigit() }) {
            Toast.makeText(context, "Phone number must be exactly 10 digits", Toast.LENGTH_SHORT).show()
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
            val ageInt = age.toIntOrNull() ?: 0
            val request = RegisterRequest(fullName, email, password, phoneNumber, ageInt)
            RetrofitClient.instance.register(request).enqueue(object : Callback<AuthResponse> {
                override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                    isLoading = false
                    if (response.isSuccessful) {
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
                        val errorMsg = response.errorBody()?.string() ?: "Registration failed"
                        Toast.makeText(context, "Error: $errorMsg", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    isLoading = false
                    Toast.makeText(context, "Connection failed: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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

            // Title
            Text(
                text = "Create Your Account",
                color = Color.Black,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "Start your CS journey with us",
                color = Color.Black.copy(alpha = 0.6f),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Full Name
            SignUpTextField(
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = "Full Name",
                icon = Icons.Outlined.Person,
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Phone Number
            SignUpTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                placeholder = "Phone Number",
                icon = Icons.Outlined.Phone,
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Age
            SignUpTextField(
                value = age,
                onValueChange = { age = it },
                placeholder = "Age",
                icon = Icons.Outlined.DateRange,
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email
            SignUpTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = "Email address",
                icon = Icons.Outlined.Email,
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password
            SignUpTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = "Password",
                icon = Icons.Outlined.Lock,
                isPasswordField = true,
                passwordVisible = passwordVisible,
                onPasswordToggle = { passwordVisible = !passwordVisible },
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Password
            SignUpTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = "Confirm Password",
                icon = Icons.Outlined.Lock,
                isPasswordField = true,
                passwordVisible = confirmPasswordVisible,
                onPasswordToggle = { confirmPasswordVisible = !confirmPasswordVisible },
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Sign Up Button
            Button(
                onClick = { handleSignUp() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SplashPrimary),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Sign Up", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Spacer(modifier = Modifier.height(32.dp))

            // Footer
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text("Already have an account?", color = Color.Black.copy(alpha = 0.6f), fontSize = 14.sp)
                TextButton(onClick = onLoginClick) {
                    Text("Login", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun SignUpTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector,
    isPasswordField: Boolean = false,
    passwordVisible: Boolean = false,
    onPasswordToggle: () -> Unit = {},
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(placeholder) },
        leadingIcon = {
            Icon(imageVector = icon, contentDescription = null, tint = SplashTagline)
        },
        trailingIcon = if (isPasswordField) {
            {
                IconButton(onClick = onPasswordToggle) {
                    Text(if (passwordVisible) "👁️" else "👁️‍🗨️")
                }
            }
        } else null,
        visualTransformation = if (isPasswordField && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color(0xFFE0E0E0),
            focusedBorderColor = SplashPrimary,
            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
            disabledTextColor = MaterialTheme.colorScheme.onBackground,
            errorTextColor = MaterialTheme.colorScheme.onBackground
        ),
        singleLine = true,
        enabled = enabled
    )
}
