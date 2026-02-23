package com.simats.pathpiolet.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.simats.pathpiolet.ui.theme.SplashPrimary
import com.simats.pathpiolet.ui.theme.SplashTagline
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.foundation.clickable
import com.simats.pathpiolet.utils.SessionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    val username = sessionManager.getUsername()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        // Header Section
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            color = SplashPrimary,
            shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
        ) {
            Column(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(24.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Welcome, $username! 👋",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Your CS Career Roadmap Starts Here",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Search Bar
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clickable {
                            context.startActivity(android.content.Intent(context, QnaActivity::class.java))
                        },
                    shape = RoundedCornerShape(28.dp),
                    color = Color.White.copy(alpha = 0.1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Icon(
                            Icons.Default.Search, 
                            contentDescription = "Search for AI assistance", 
                            tint = Color.White.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Search for AI assistance...",
                            color = Color.White.copy(alpha = 0.4f),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // AI College Finder Card
        Surface(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF4D6FFF) // Bright blue
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(64.dp),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.2f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color.White)
                    }
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column {
                    Text(
                        text = "AI College Finder",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Get personalized college\nrecommendations based on\nyour preferences",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    val context = LocalContext.current
                    Button(
                        onClick = {
                             context.startActivity(android.content.Intent(context, com.simats.pathpiolet.ui.PreferencesActivity::class.java))
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text("Start AI Search →", color = Color(0xFF4D6FFF), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Text(
            text = "Explore Pathways",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        // Grid of Pathways
        Column(modifier = Modifier.padding(16.dp)) {
            val context = LocalContext.current
            
            Row(modifier = Modifier.fillMaxWidth()) {
                PathwayCard(
                    modifier = Modifier.weight(1f),
                    icon = painterResource(id = com.simats.pathpiolet.R.drawable.ic_book),
                    title = "Explore After\n10th",
                    iconBg = Color(0xFFE8EAF6),
                    onClick = {
                        context.startActivity(android.content.Intent(context, FoundationActivity::class.java))
                    }
                )
                Spacer(modifier = Modifier.width(16.dp))
                PathwayCard(
                    modifier = Modifier.weight(1f),
                    icon = painterResource(id = com.simats.pathpiolet.R.drawable.ic_school), // Cap
                    title = "Explore After\n12th",
                    iconBg = Color(0xFFFFF9C4),
                    onClick = {
                        context.startActivity(android.content.Intent(context, PreparationActivity::class.java))
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                PathwayCard(
                    modifier = Modifier.weight(1f),
                    icon = rememberVectorPainter(image = Icons.Default.Home), // Building/College Icon
                    title = "Top Colleges",
                    iconBg = Color(0xFFE8EAF6),
                    onClick = {
                        context.startActivity(android.content.Intent(context, CollegesActivity::class.java))
                    }
                )
                Spacer(modifier = Modifier.width(16.dp))
                PathwayCard(
                    modifier = Modifier.weight(1f),
                    icon = painterResource(id = com.simats.pathpiolet.R.drawable.ic_trending_up), // Chart
                    title = "Future\nRoadmap",
                    iconBg = Color(0xFFFFF9C4),
                    onClick = {
                         context.startActivity(android.content.Intent(context, RoadmapActivity::class.java))
                    }
                )
            }
        }
    }
}

@Composable
fun PathwayCard(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.painter.Painter,
    title: String,
    iconBg: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(160.dp)
            .clickable(onClick = onClick), // Add clickable
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(56.dp),
                shape = RoundedCornerShape(16.dp),
                color = iconBg
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = icon, 
                        contentDescription = null, 
                        tint = SplashPrimary, 
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                lineHeight = 18.sp
            )
        }
    }
}
