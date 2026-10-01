package com.example.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.Assignment
import com.example.data.model.AssignmentStatus
import com.example.data.model.Priority
import com.example.ui.theme.PriorityCriticalColor
import com.example.ui.theme.PriorityHighColor
import com.example.ui.theme.PriorityLowColor
import com.example.ui.theme.PriorityMediumColor
import com.example.ui.theme.StatusSubmittedColor
import com.example.ui.viewmodel.TermTrackerViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@Composable
fun CalendarScreen(
    viewModel: TermTrackerViewModel
) {
    val allAssignments by viewModel.allAssignments.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedCalendarDate.collectAsStateWithLifecycle()

    // Initialize calendar to October 2026 (Sep 2026 Term start)
    var currentYear by remember { mutableStateOf(2026) }
    var currentMonth by remember { mutableStateOf(Calendar.OCTOBER) } // 0-based: 9 is October

    val monthFormatter = remember { SimpleDateFormat("MMMM yyyy", Locale.US) }
    val displayCalendar = remember(currentYear, currentMonth) {
        Calendar.getInstance(TimeZone.getTimeZone("Asia/Kolkata")).apply {
            set(Calendar.YEAR, currentYear)
            set(Calendar.MONTH, currentMonth)
            set(Calendar.DAY_OF_MONTH, 1)
        }
    }

    val daysInMonth = displayCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfWeek = displayCalendar.get(Calendar.DAY_OF_WEEK) // 1=Sunday, 2=Monday, ...

    // Map dates in "YYYY-MM-DD" to assignments
    val assignmentsByDate = remember(allAssignments) {
        allAssignments.groupBy { it.dueDate }
    }

    // Selected day's assignments
    val selectedDayAssignments = assignmentsByDate[selectedDate] ?: emptyList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Month Selector Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Calendar View",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Visualize May 2026 Term Deadlines",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = {
                        if (currentMonth == Calendar.JANUARY) {
                            currentMonth = Calendar.DECEMBER
                            currentYear--
                        } else {
                            currentMonth--
                        }
                    },
                    modifier = Modifier.testTag("prev_month_button")
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Month")
                }

                Text(
                    text = monthFormatter.format(displayCalendar.time),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                IconButton(
                    onClick = {
                        if (currentMonth == Calendar.DECEMBER) {
                            currentMonth = Calendar.JANUARY
                            currentYear++
                        } else {
                            currentMonth++
                        }
                    },
                    modifier = Modifier.testTag("next_month_button")
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Month")
                }
            }
        }

        // Quick Milestones row
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val keyDates = listOf(
                "Term Start (Oct 2)" to "2026-10-02",
                "W1 Due (Oct 11)" to "2026-10-11",
                "Quiz 1 (Nov 15)" to "2026-11-15",
                "OPPE 1 (Nov 22)" to "2026-11-22",
                "Quiz 2 (Dec 5)" to "2026-12-05",
                "OPPE 2 (Dec 20)" to "2026-12-20",
                "OPPE 2 Day 2 (Jan 3)" to "2027-01-03",
                "End Term (Jan 10)" to "2027-01-10"
            )
            items(keyDates) { (label, date) ->
                FilterChip(
                    selected = selectedDate == date,
                    onClick = {
                        viewModel.setSelectedCalendarDate(date)
                        val parts = date.split("-")
                        currentYear = parts[0].toInt()
                        currentMonth = parts[1].toInt() - 1
                    },
                    label = { Text(label) }
                )
            }
        }

        // Calendar Grid Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                // Days of week header
                Row(modifier = Modifier.fillMaxWidth()) {
                    val days = listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa")
                    days.forEach { day ->
                        Text(
                            text = day,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Days cells
                val totalCells = ((firstDayOfWeek - 1) + daysInMonth + 6) / 7 * 7
                val weeksCount = totalCells / 7

                for (week in 0 until weeksCount) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        for (dayOfWeek in 1..7) {
                            val cellIndex = week * 7 + dayOfWeek
                            val dayNumber = cellIndex - (firstDayOfWeek - 1)

                            if (dayNumber in 1..daysInMonth) {
                                val monthStr = String.format("%02d", currentMonth + 1)
                                val dayStr = String.format("%02d", dayNumber)
                                val cellDate = "$currentYear-$monthStr-$dayStr"
                                val isSelected = cellDate == selectedDate
                                val dayAssignments = assignmentsByDate[cellDate] ?: emptyList()
                                val hasCritical = dayAssignments.any { it.priority == Priority.CRITICAL }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .padding(2.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when {
                                                isSelected -> MaterialTheme.colorScheme.primary
                                                dayAssignments.isNotEmpty() -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                                                else -> Color.Transparent
                                            }
                                        )
                                        .border(
                                            width = if (isSelected) 2.dp else 0.dp,
                                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else Color.Transparent,
                                            shape = CircleShape
                                        )
                                        .clickable {
                                            viewModel.setSelectedCalendarDate(cellDate)
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = dayNumber.toString(),
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = if (dayAssignments.isNotEmpty() || isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = when {
                                                isSelected -> MaterialTheme.colorScheme.onPrimary
                                                hasCritical -> PriorityCriticalColor
                                                dayAssignments.isNotEmpty() -> MaterialTheme.colorScheme.onPrimaryContainer
                                                else -> MaterialTheme.colorScheme.onSurface
                                            }
                                        )

                                        if (dayAssignments.isNotEmpty()) {
                                            Box(
                                                modifier = Modifier
                                                    .size(4.dp)
                                                    .clip(CircleShape)
                                                    .background(
                                                        if (isSelected) MaterialTheme.colorScheme.onPrimary
                                                        else if (hasCritical) PriorityCriticalColor
                                                        else MaterialTheme.colorScheme.primary
                                                    )
                                            )
                                        }
                                    }
                                }
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }

        // Selected Date Details Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Deadlines on $selectedDate",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${selectedDayAssignments.size} items",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (selectedDayAssignments.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Event,
                        contentDescription = null,
                        modifier = Modifier.size(36.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "No deadlines on this date",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Select another highlighted date to inspect deadlines.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(selectedDayAssignments) { assignment ->
                    CalendarAssignmentItem(
                        assignment = assignment,
                        onToggle = {
                            val next = when (assignment.status) {
                                AssignmentStatus.PENDING -> AssignmentStatus.IN_PROGRESS
                                AssignmentStatus.IN_PROGRESS -> AssignmentStatus.SUBMITTED
                                AssignmentStatus.SUBMITTED -> AssignmentStatus.PENDING
                            }
                            viewModel.updateAssignmentStatus(assignment.id, next)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CalendarAssignmentItem(
    assignment: Assignment,
    onToggle: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        color = when (assignment.priority) {
                            Priority.CRITICAL -> PriorityCriticalColor
                            Priority.HIGH -> PriorityHighColor
                            Priority.MEDIUM -> PriorityMediumColor
                            Priority.LOW -> PriorityLowColor
                        },
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = assignment.priority.displayName,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = assignment.subjectName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = assignment.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )

                if (assignment.notes.isNotBlank()) {
                    Text(
                        text = assignment.notes,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onToggle,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = when (assignment.status) {
                        AssignmentStatus.SUBMITTED -> StatusSubmittedColor
                        AssignmentStatus.IN_PROGRESS -> MaterialTheme.colorScheme.secondary
                        AssignmentStatus.PENDING -> MaterialTheme.colorScheme.outline
                    }
                ),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = assignment.status.displayName,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
