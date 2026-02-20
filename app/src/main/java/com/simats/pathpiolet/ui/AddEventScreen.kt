package com.simats.pathpiolet.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.pathpiolet.Event
import com.simats.pathpiolet.ui.theme.SplashPrimary
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventScreen(
    selectedDate: LocalDate,
    onBack: () -> Unit,
    onSave: (Event) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Add Event", fontWeight = FontWeight.Bold, color = SplashPrimary)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = SplashPrimary)
                    }
                },
                actions = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            title = ""
                            description = ""
                            time = ""
                        },
                        modifier = Modifier.weight(1f).height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
                    ) {
                        Text("Clear", color = Color.Gray)
                    }
                    
                    Button(
                        onClick = {
                            if (title.isNotEmpty()) {
                                onSave(Event(title = title, description = description, date = selectedDate, time = time))
                            }
                        },
                        enabled = title.isNotEmpty(),
                        modifier = Modifier.weight(1f).height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4D6FFF)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Save", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF4F7FF))
                .verticalScroll(scrollState)
                .padding(24.dp)
        ) {
            // Selected Date (Read-only styled box)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFE8EAF6).copy(alpha = 0.5f),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFC5CAE9))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Selected Date", fontSize = 12.sp, color = Color.Gray)
                    Text(
                        text = selectedDate.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = SplashPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Event Title
            Text(
                text = buildAnnotatedString {
                    append("Event Title ")
                    pushStyle(SpanStyle(color = Color.Red))
                    append("*")
                    pop()
                },
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = SplashPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                placeholder = { Text("Enter event title", color = Color.LightGray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    unfocusedBorderColor = Color(0xFFE0E0E0)
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Description
            Text(
                text = "Description (optional)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = SplashPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = { Text("Enter description", color = Color.LightGray) },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    unfocusedBorderColor = Color(0xFFE0E0E0)
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Time
            Text(
                text = "Time (optional)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = SplashPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = time,
                onValueChange = { time = it },
                placeholder = { Text("e.g. 09:00 AM", color = Color.LightGray) },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null, tint = Color.Gray) }, // Using standard icon
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    unfocusedBorderColor = Color(0xFFE0E0E0)
                )
            )
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// Utility to match screenshot's red asterisk
@Composable
fun buildAnnotatedString(content: androidx.compose.ui.text.AnnotatedString.Builder.() -> Unit) = 
    androidx.compose.ui.text.buildAnnotatedString(content)

typealias SpanStyle = androidx.compose.ui.text.SpanStyle
