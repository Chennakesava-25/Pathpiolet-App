package com.simats.pathpiolet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.simats.pathpiolet.ui.theme.PathPioletTheme

import androidx.compose.runtime.*
import com.simats.pathpiolet.ui.AboutUsActivity
import com.simats.pathpiolet.ui.ActivityHistoryActivity
import com.simats.pathpiolet.ui.AddEventScreen
import com.simats.pathpiolet.ui.CalendarScreen
import com.simats.pathpiolet.ui.CheckEmailScreen
import com.simats.pathpiolet.ui.CollegesScreen
import com.simats.pathpiolet.ui.ContactUsActivity
import com.simats.pathpiolet.ui.EditProfileActivity
import com.simats.pathpiolet.ui.ForgotPasswordScreen
import com.simats.pathpiolet.ui.HomeScreen
import com.simats.pathpiolet.ui.LoginScreen
import com.simats.pathpiolet.ui.ProfileScreen
import com.simats.pathpiolet.ui.RoadmapScreen
import com.simats.pathpiolet.ui.SettingsActivity
import com.simats.pathpiolet.ui.SignUpScreen
import com.simats.pathpiolet.ui.SplashScreen
import kotlinx.coroutines.delay
import java.time.LocalDate
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Event(
    val id: Int = -1,
    val title: String,
    val description: String = "",
    val date: LocalDate,
    val time: String = ""
)

enum class Screen {
    Splash, Login, SignUp, ForgotPassword, VerifyOtp, SignUpVerifyOtp, ResetPassword, Home, Colleges, Roadmap, Calendar, Profile, AddEvent
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Force Light Mode globally
        androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO)
        
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PathPioletTheme {
                var currentScreen by remember { mutableStateOf(Screen.Splash) }
                var resetEmail by remember { mutableStateOf("") }
                var events by remember { mutableStateOf(listOf<Event>()) }
                var selectedDate by remember { mutableStateOf(LocalDate.now()) }

                val mainTabs = listOf(Screen.Home, Screen.Colleges, Screen.Roadmap, Screen.Calendar, Screen.Profile)
                val context = LocalContext.current
                val sessionManager = remember { com.simats.pathpiolet.utils.SessionManager(context) }
                val userId = sessionManager.getUserId()
                
                // Add a refresh trigger to force recomposition when activity is resumed
                var refreshTrigger by remember { mutableIntStateOf(0) }
                
                DisposableEffect(Unit) {
                    val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
                        if (event == androidx.lifecycle.Lifecycle.Event.ON_RESUME) {
                            refreshTrigger++
                        }
                    }
                    val lifecycle = (context as androidx.activity.ComponentActivity).lifecycle
                    lifecycle.addObserver(observer)
                    onDispose {
                        lifecycle.removeObserver(observer)
                    }
                }

                val showBottomBar = currentScreen in mainTabs

                LaunchedEffect(currentScreen) {
                    if (currentScreen == Screen.Splash) {
                        delay(3000)
                        currentScreen = if (userId != -1) Screen.Home else Screen.Login
                    }
                }

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            NavigationBar(
                                containerColor = Color.White,
                                tonalElevation = 8.dp
                            ) {
                                NavigationBarItem(
                                    selected = currentScreen == Screen.Home,
                                    onClick = { currentScreen = Screen.Home },
                                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                                    label = { Text("Home") },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color(0xFFFBC02D),
                                        selectedTextColor = Color(0xFFFBC02D),
                                        unselectedIconColor = Color(0xFF7C86A2),
                                        unselectedTextColor = Color(0xFF7C86A2),
                                        indicatorColor = Color.Transparent
                                    )
                                )
                                NavigationBarItem(
                                    selected = currentScreen == Screen.Colleges,
                                    onClick = { currentScreen = Screen.Colleges },
                                    icon = { Icon(Icons.Outlined.Build, contentDescription = "Colleges") },
                                    label = { Text("Colleges") },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color(0xFFFBC02D),
                                        selectedTextColor = Color(0xFFFBC02D),
                                        unselectedIconColor = Color(0xFF7C86A2),
                                        unselectedTextColor = Color(0xFF7C86A2),
                                        indicatorColor = Color.Transparent
                                    )
                                )
                                NavigationBarItem(
                                    selected = currentScreen == Screen.Roadmap,
                                    onClick = { currentScreen = Screen.Roadmap },
                                    icon = { Icon(Icons.Outlined.Place, contentDescription = "Roadmap") },
                                    label = { Text("Roadmap") },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color(0xFFFBC02D),
                                        selectedTextColor = Color(0xFFFBC02D),
                                        unselectedIconColor = Color(0xFF7C86A2),
                                        unselectedTextColor = Color(0xFF7C86A2),
                                        indicatorColor = Color.Transparent
                                    )
                                )
                                NavigationBarItem(
                                    selected = currentScreen == Screen.Calendar,
                                    onClick = { currentScreen = Screen.Calendar },
                                    icon = {
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = if (currentScreen == Screen.Calendar) Color(0xFFFBC02D).copy(alpha = 0.1f) else Color(0xFFF5F5F5),
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Text(
                                                    LocalDate.now().dayOfMonth.toString(),
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (currentScreen == Screen.Calendar) Color(0xFFFBC02D) else Color(0xFF7C86A2)
                                                )
                                            }
                                        }
                                    },
                                    label = { Text("Calendar") },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedTextColor = Color(0xFFFBC02D),
                                        unselectedTextColor = Color(0xFF7C86A2),
                                        indicatorColor = Color.Transparent
                                    )
                                )
                                NavigationBarItem(
                                    selected = currentScreen == Screen.Profile,
                                    onClick = { currentScreen = Screen.Profile },
                                    icon = { Icon(Icons.Outlined.Person, contentDescription = "Profile") },
                                    label = { Text("Profile") },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color(0xFFFBC02D),
                                        selectedTextColor = Color(0xFFFBC02D),
                                        unselectedIconColor = Color(0xFF7C86A2),
                                        unselectedTextColor = Color(0xFF7C86A2),
                                        indicatorColor = Color.Transparent
                                    )
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                top = innerPadding.calculateTopPadding(),
                                bottom = if (showBottomBar) innerPadding.calculateBottomPadding() else 0.dp
                            ),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        when (currentScreen) {
                            Screen.Splash -> SplashScreen()
                            Screen.Login -> LoginScreen(
                                onSignUpClick = { currentScreen = Screen.SignUp },
                                onForgotPasswordClick = { currentScreen = Screen.ForgotPassword },
                                onLoginSuccess = { currentScreen = Screen.Home }
                            )
                            Screen.SignUp -> SignUpScreen(
                                onLoginClick = { currentScreen = Screen.Login },
                                onSignUpSuccess = { email ->
                                    resetEmail = email 
                                    currentScreen = Screen.SignUpVerifyOtp
                                }
                            )
                            Screen.ForgotPassword -> com.simats.pathpiolet.ui.ForgotPasswordScreen(
                                onBackToLogin = { currentScreen = Screen.Login },
                                onOtpSent = { email ->
                                    resetEmail = email
                                    currentScreen = Screen.VerifyOtp
                                }
                            )
                            Screen.VerifyOtp -> com.simats.pathpiolet.ui.VerifyOtpScreen(
                                email = resetEmail,
                                isFromSignUp = false,
                                onBack = { currentScreen = Screen.ForgotPassword },
                                onOtpVerified = { currentScreen = Screen.ResetPassword }
                            )
                            Screen.SignUpVerifyOtp -> com.simats.pathpiolet.ui.VerifyOtpScreen(
                                email = resetEmail,
                                isFromSignUp = true,
                                onBack = { currentScreen = Screen.SignUp },
                                onOtpVerified = {},
                                onSignUpSuccess = { currentScreen = Screen.Home }
                            )
                            Screen.ResetPassword -> com.simats.pathpiolet.ui.ResetPasswordScreen(
                                email = resetEmail,
                                onPasswordReset = { currentScreen = Screen.Login }
                            )
                            Screen.Home -> HomeScreen(
                                modifier = Modifier
                            )
                            Screen.Colleges -> CollegesScreen()
                            Screen.Roadmap -> RoadmapScreen()
                            Screen.Profile -> ProfileScreen(
                                onLogout = {
                                    sessionManager.clearSession()
                                    currentScreen = Screen.Login
                                }
                            )
                            Screen.Calendar -> com.simats.pathpiolet.ui.CalendarScreen(
                                events = events,
                                userId = userId,
                                onBack = { currentScreen = Screen.Home },
                                onAddEventClick = { date ->
                                    selectedDate = date
                                    currentScreen = Screen.AddEvent
                                },
                                onEventsUpdated = { updatedEvents ->
                                    events = updatedEvents
                                }
                            )
                            Screen.AddEvent -> com.simats.pathpiolet.ui.AddEventScreen(
                                selectedDate = selectedDate,
                                userId = userId,
                                onBack = { currentScreen = Screen.Calendar },
                                onSaveSuccess = { 
                                    currentScreen = Screen.Calendar
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PathPioletTheme {
        Greeting("Android")
    }
}