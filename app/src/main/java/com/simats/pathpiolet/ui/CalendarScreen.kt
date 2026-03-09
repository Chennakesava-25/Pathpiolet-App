package com.simats.pathpiolet.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.pathpiolet.Event
import com.simats.pathpiolet.ui.theme.SplashPrimary
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale
import android.widget.Toast
import com.simats.pathpiolet.api.RetrofitClient
import com.simats.pathpiolet.api.EventData
import com.simats.pathpiolet.api.AuthResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.ui.platform.LocalContext

import com.simats.pathpiolet.ui.components.StandardBackButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    events: List<Event>,
    userId: Int,
    onBack: () -> Unit,
    onAddEventClick: (LocalDate) -> Unit,
    onEventsUpdated: (List<Event>) -> Unit
) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showDeleteDialog by remember { mutableStateOf<Event?>(null) }
    val context = LocalContext.current

    val refreshEvents = {
        if (userId != -1) {
            RetrofitClient.instance.getEvents(userId).enqueue(object : Callback<List<EventData>> {
                override fun onResponse(call: Call<List<EventData>>, response: Response<List<EventData>>) {
                    if (response.isSuccessful) {
                        val fetchedEvents = response.body()?.map { data ->
                            Event(
                                id = data.id,
                                title = data.title,
                                description = data.description ?: "",
                                date = LocalDate.parse(data.event_date),
                                time = data.time ?: ""
                            )
                        } ?: emptyList()
                        onEventsUpdated(fetchedEvents)
                    }
                }
                override fun onFailure(call: Call<List<EventData>>, t: Throwable) {
                    Toast.makeText(context, "Failed to load events", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    LaunchedEffect(userId) {
        refreshEvents()
    }

    Scaffold(
        topBar = {
            Surface(
                color = SplashPrimary,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    StandardBackButton(onClick = onBack, modifier = Modifier.padding(start = 0.dp))
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Calendar",
                                color = Color.White,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Track exams & important deadlines",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 14.sp
                            )
                        }
                        
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.size(60.dp, 70.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    selectedDate.format(DateTimeFormatter.ofPattern("MMM")),
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    selectedDate.dayOfMonth.toString(),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SplashPrimary
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { 
                    if (selectedDate.isBefore(LocalDate.now())) {
                        Toast.makeText(context, "Cannot add events for past dates", Toast.LENGTH_SHORT).show()
                    } else {
                        onAddEventClick(selectedDate)
                    }
                },
                containerColor = Color(0xFFFBC02D),
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Event", tint = Color.White)
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF4F7FF))
        ) {
            item {
                CalendarCard(
                    currentMonth = currentMonth,
                    selectedDate = selectedDate,
                    onDateSelected = { selectedDate = it },
                    onMonthChange = { currentMonth = it },
                    events = events
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = SplashPrimary.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Default.DateRange, 
                                contentDescription = null, 
                                tint = SplashPrimary,
                                modifier = Modifier.padding(6.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Upcoming Events",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = SplashPrimary
                        )
                    }
                }
            }

            if (events.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "No upcoming events. Tap + to add one.",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                items(events) { event ->
                    EventCard(event = event, onDelete = { showDeleteDialog = it })
                }
            }
            
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }

        if (showDeleteDialog != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = null },
                title = { Text("Delete Event?") },
                text = { Text("Are you sure you want to delete this event?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val eventToDelete = showDeleteDialog!!
                            RetrofitClient.instance.deleteEvent(eventToDelete.id).enqueue(object : Callback<AuthResponse> {
                                override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                                    if (response.isSuccessful) {
                                        refreshEvents()
                                        Toast.makeText(context, "Event deleted", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                                    Toast.makeText(context, "Delete failed", Toast.LENGTH_SHORT).show()
                                }
                            })
                            showDeleteDialog = null
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = null }) {
                        Text("Cancel")
                    }
                },
                shape = RoundedCornerShape(24.dp),
                containerColor = Color.White
            )
        }
    }
}

@Composable
fun CalendarCard(
    currentMonth: YearMonth,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onMonthChange: (YearMonth) -> Unit,
    events: List<Event>
) {
    Surface(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onMonthChange(currentMonth.minusMonths(1)) }) {
                    Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Prev", tint = SplashPrimary)
                }
                Text(
                    text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)} ${currentMonth.year}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = SplashPrimary
                )
                IconButton(onClick = { onMonthChange(currentMonth.plusMonths(1)) }) {
                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Next", tint = SplashPrimary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Weekdays
            Row(modifier = Modifier.fillMaxWidth()) {
                val days = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
                days.forEach { day ->
                    Text(
                        text = day,
                        modifier = Modifier.weight(1f),
                        fontSize = 12.sp,
                        color = Color.Gray,
                        align = Alignment.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Calendar Days
            val firstDayOfMonth = currentMonth.atDay(1)
            val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7 // 0 for Sunday
            val daysInMonth = currentMonth.lengthOfMonth()
            
            // Previous month padding
            val prevMonth = currentMonth.minusMonths(1)
            val daysInPrevMonth = prevMonth.lengthOfMonth()

            var dayCount = 1
            for (week in 0..5) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (dayOfWeek in 0..6) {
                        val date: LocalDate? = when {
                            week == 0 && dayOfWeek < firstDayOfWeek -> {
                                prevMonth.atDay(daysInPrevMonth - firstDayOfWeek + dayOfWeek + 1)
                            }
                            dayCount <= daysInMonth -> {
                                currentMonth.atDay(dayCount++)
                            }
                            else -> {
                                currentMonth.plusMonths(1).atDay(dayCount++ - daysInMonth)
                            }
                        }
                        
                        val isSelected = date == selectedDate
                        val hasEvent = events.any { it.date == date }
                        val isCurrentMonth = date?.month == currentMonth.month

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(2.dp)
                                .clickable { date?.let { onDateSelected(it) } },
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Surface(
                                    modifier = Modifier.size(40.dp),
                                    shape = CircleShape,
                                    color = Color(0xFFFBC02D)
                                ) {}
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = date?.dayOfMonth?.toString() ?: "",
                                    fontSize = 14.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = when {
                                        isSelected -> Color.White
                                        !isCurrentMonth -> Color.LightGray
                                        else -> Color.DarkGray
                                    }
                                )
                                if (hasEvent && !isSelected) {
                                    Surface(
                                        modifier = Modifier.size(4.dp),
                                        shape = CircleShape,
                                        color = Color.Red
                                    ) {}
                                }
                            }
                        }
                    }
                }
                if (dayCount > daysInMonth && week >= 4) break
            }
        }
    }
}

@Composable
fun EventCard(event: Event, onDelete: (Event) -> Unit) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = event.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = SplashPrimary
                )
                if (event.description.isNotEmpty()) {
                    Text(
                        text = event.description,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DateRange, contentDescription = null, size = 14.sp, tint = Color.Gray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = event.date.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    if (event.time.isNotEmpty()) {
                        Spacer(modifier = Modifier.width(16.dp))
                        Icon(Icons.Default.PlayArrow, contentDescription = null, size = 14.sp, tint = Color.Gray) // Repurposed for time
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = event.time,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                val daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), event.date)
                Surface(
                    color = Color(0xFFFBC02D),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "$daysLeft\ndays left",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        align = Alignment.Center
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                IconButton(onClick = { onDelete(event) }, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Outlined.Delete, contentDescription = "Delete", tint = Color.Red)
                }
            }
        }
    }
}

// Helper for centering text in week view
@Composable
fun Text(text: String, modifier: Modifier, fontSize: androidx.compose.ui.unit.TextUnit, color: Color, align: Alignment, fontWeight: FontWeight = FontWeight.Normal) {
    Box(modifier = modifier, contentAlignment = align) {
        androidx.compose.material3.Text(text = text, fontSize = fontSize, color = color, fontWeight = fontWeight)
    }
}

// Helper for icon size
@Composable
fun Icon(imageVector: ImageVector, contentDescription: String?, size: androidx.compose.ui.unit.TextUnit, tint: Color) {
    androidx.compose.material3.Icon(
        imageVector = imageVector, 
        contentDescription = contentDescription, 
        modifier = Modifier.size(size.value.dp),
        tint = tint
    )
}
