package com.simats.pathpiolet.ui

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.automirrored.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import com.simats.pathpiolet.MainActivity
import com.simats.pathpiolet.Screen
import com.simats.pathpiolet.api.RetrofitClient
import com.simats.pathpiolet.ui.theme.SplashPrimary
import com.simats.pathpiolet.utils.SessionManager

@Composable
fun ProfileScreen(onLogout: () -> Unit) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val sessionManager = SessionManager(context)
    val username = sessionManager.getUsername()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(24.dp)
    ) {
        // App Header
        Text(
            text = "PathPoilet",
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Profile Card
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                color = Color(0xFF8E99D6),
                shadowElevation = 4.dp
            ) {
                val profilePic = sessionManager.getProfilePicture()
                if (!profilePic.isNullOrEmpty()) {
                    AsyncImage(
                        model = RetrofitClient.BASE_URL.dropLast(1) + profilePic,
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = username.take(1).uppercase(),
                            color = Color.White,
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = username,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = sessionManager.getEducation().ifBlank { "Student" },
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Menu Items
        ProfileMenuItem(
            icon = Icons.Outlined.Edit,
            title = "Edit profile",
            onClick = { context.startActivity(Intent(context, EditProfileActivity::class.java)) }
        )
        ProfileMenuItem(
            icon = Icons.AutoMirrored.Outlined.List,
            title = "My activity",
            onClick = { context.startActivity(Intent(context, ActivityHistoryActivity::class.java)) }
        )
        ProfileMenuItem(
            icon = Icons.Outlined.Info,
            title = "About Us",
            onClick = { context.startActivity(Intent(context, AboutUsActivity::class.java)) }
        )
        ProfileMenuItem(
            icon = Icons.Outlined.Call,
            title = "Contact Us",
            onClick = { context.startActivity(Intent(context, ContactUsActivity::class.java)) }
        )
        ProfileMenuItem(
            icon = Icons.Outlined.Settings,
            title = "Settings",
            onClick = { context.startActivity(Intent(context, SettingsActivity::class.java)) }
        )
        
        Spacer(modifier = Modifier.height(12.dp))

        ProfileMenuItem(
            icon = Icons.AutoMirrored.Outlined.ExitToApp,
            title = "Logout",
            titleColor = Color.Red,
            iconContainerColor = Color(0xFFFFF0F0),
            onClick = onLogout
        )
        
        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    titleColor: Color = Color.Black,
    iconContainerColor: Color = Color.White,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(12.dp),
                color = iconContainerColor
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (titleColor == Color.Red) Color.Red else SplashPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                text = title,
                color = if (titleColor == Color.Red) Color.Red else MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFFD1D5DB)
            )
        }
    }
}
