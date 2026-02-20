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

    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .background(Color.White)
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
                color = SplashPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "Start your CS journey with us",
                color = SplashTagline,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Full Name
            SignUpTextField(
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = "Full Name",
                icon = Icons.Outlined.Person
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Phone Number
            SignUpTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                placeholder = "Phone Number",
                icon = Icons.Outlined.Phone
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Age
            SignUpTextField(
                value = age,
                onValueChange = { age = it },
                placeholder = "Age",
                icon = Icons.Outlined.DateRange // Best match for age/date
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email
            SignUpTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = "Email address",
                icon = Icons.Outlined.Email
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
                onPasswordToggle = { passwordVisible = !passwordVisible }
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
                onPasswordToggle = { confirmPasswordVisible = !confirmPasswordVisible }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Sign Up Button
            Button(
                onClick = onSignUpSuccess,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SplashPrimary),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text("Sign Up", fontSize = 18.sp, fontWeight = FontWeight.Bold)
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

            // Google Sign Up
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
                    Text("G", color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sign Up with Google", color = SplashTagline, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Footer
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text("Already have an account?", color = SplashTagline, fontSize = 14.sp)
                TextButton(onClick = onLoginClick) {
                    Text("Login", color = SplashPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
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
    onPasswordToggle: () -> Unit = {}
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
            focusedBorderColor = SplashPrimary
        ),
        singleLine = true
    )
}
