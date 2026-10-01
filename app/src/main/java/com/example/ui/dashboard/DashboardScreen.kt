package com.example.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.Assignment
import com.example.data.model.AssignmentStatus
import com.example.data.model.Priority
import com.example.ui.assignments.AddEditAssignmentDialog
import com.example.ui.theme.PriorityCriticalColor
import com.example.ui.theme.PriorityHighColor
import com.example.ui.theme.PriorityLowColor
import com.example.ui.theme.PriorityMediumColor
import com.example.ui.theme.StatusSubmittedColor
import com.example.ui.viewmodel.TermTrackerViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: TermTrackerViewModel,
    onNavigateToSubjects: () -> Unit,
    onNavigateToGradeCalc: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val allAssignments by viewModel.allAssignments.collectAsStateWithLifecycle()
    val activeAssignments by viewModel.activeAssignments.collectAsStateWithLifecycle()
    val enrolledSubjects by viewModel.enrolledSubjects.collectAsStateWithLifecycle()
    val selectedSubjectFilter by viewModel.selectedSubjectFilter.collectAsStateWithLifecycle()
    val selectedPriorityFilter by viewModel.selectedPriorityFilter.collectAsStateWithLifecycle()

    var showAddDialog by remember { mutableStateOf(false) }
    var editingAssignment by remember { mutableStateOf<Assignment?>(null) }

    // Metrics
    val completedCount = allAssignments.count { it.status == AssignmentStatus.SUBMITTED }
    val criticalCount = activeAssignments.count { it.priority == Priority.CRITICAL }
    val highCount = activeAssignments.count { it.priority == Priority.HIGH }
    val nextUpcoming = activeAssignments.firstOrNull()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.testTag("add_assignment_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Assignment")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "TermTracker",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "IITM BS Sep 2026 Term • ${enrolledSubjects.size} Subjects",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Test Reminder Button
                    IconButton(
                        onClick = {
                            viewModel.sendTestNotification(context)
                            viewModel.triggerCheckReminders(context)
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Automated reminder check triggered & test alert sent!")
                            }
                        },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .testTag("test_notification_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = "Trigger Reminder Check",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            // Hero Next Deadline Card
            if (nextUpcoming != null) {
                item {
                    val now = System.currentTimeMillis()
                    val diff = nextUpcoming.dueTimestamp - now
                    val daysLeft = TimeUnit.MILLISECONDS.toDays(diff)
                    val hoursLeft = TimeUnit.MILLISECONDS.toHours(diff) % 24

                    val countdownText = when {
                        diff < 0 -> "Overdue!"
                        daysLeft == 0L && hoursLeft == 0L -> "Due in < 1 hour!"
                        daysLeft == 0L -> "Due in $hoursLeft hours"
                        else -> "Due in $daysLeft days, $hoursLeft hrs"
                    }

                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("hero_deadline_card"),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = when (nextUpcoming.priority) {
                                        Priority.CRITICAL -> PriorityCriticalColor
                                        Priority.HIGH -> PriorityHighColor
                                        Priority.MEDIUM -> PriorityMediumColor
                                        Priority.LOW -> PriorityLowColor
                                    },
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "${nextUpcoming.priority.displayName.uppercase()} PRIORITY",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Alarm,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = countdownText,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }

                            Text(
                                text = nextUpcoming.title,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )

                            Text(
                                text = "${nextUpcoming.subjectName} • Due: ${nextUpcoming.dueDate}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                            )

                            if (nextUpcoming.notes.isNotBlank()) {
                                Text(
                                    text = nextUpcoming.notes,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = {
                                        viewModel.updateAssignmentStatus(nextUpcoming.id, AssignmentStatus.SUBMITTED)
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Marked '${nextUpcoming.title}' as Submitted!")
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.testTag("mark_done_button")
                                ) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Mark as Submitted")
                                }
                            }
                        }
                    }
                }
            }

            // Quick Stats Grid
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = "Active",
                        value = activeAssignments.size.toString(),
                        subtitle = "Pending tasks",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Critical",
                        value = criticalCount.toString(),
                        subtitle = "Eligibility / Exam",
                        color = PriorityCriticalColor,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Done",
                        value = completedCount.toString(),
                        subtitle = "Submitted",
                        color = StatusSubmittedColor,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Subject Completion Rate Chart (Compose Canvas)
            if (enrolledSubjects.isNotEmpty()) {
                item {
                    SubjectCompletionChart(
                        enrolledSubjects = enrolledSubjects,
                        allAssignments = allAssignments,
                        selectedSubjectFilter = selectedSubjectFilter,
                        onSelectSubject = { viewModel.setSubjectFilter(it) }
                    )
                }
            }

            // Priority Filter Chips
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Filter by Priority",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            FilterChip(
                                selected = selectedPriorityFilter == null,
                                onClick = { viewModel.setPriorityFilter(null) },
                                label = { Text("All (${activeAssignments.size})") }
                            )
                        }
                        items(Priority.values()) { p ->
                            FilterChip(
                                selected = selectedPriorityFilter == p,
                                onClick = {
                                    viewModel.setPriorityFilter(if (selectedPriorityFilter == p) null else p)
                                },
                                label = { Text(p.displayName) }
                            )
                        }
                    }
                }
            }

            // Enrolled Subjects Filter Chips
            if (enrolledSubjects.isNotEmpty()) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Enrolled Subjects (${enrolledSubjects.size})",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Manage",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable { onNavigateToSubjects() }
                            )
                        }

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            item {
                                FilterChip(
                                    selected = selectedSubjectFilter == null,
                                    onClick = { viewModel.setSubjectFilter(null) },
                                    label = { Text("All Subjects") }
                                )
                            }
                            items(enrolledSubjects) { subj ->
                                FilterChip(
                                    selected = selectedSubjectFilter == subj.id,
                                    onClick = {
                                        viewModel.setSubjectFilter(if (selectedSubjectFilter == subj.id) null else subj.id)
                                    },
                                    label = { Text(subj.code) }
                                )
                            }
                        }
                    }
                }
            }

            // Active Assignments List
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Upcoming Deadlines",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${activeAssignments.size} tasks",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            val displayList = activeAssignments.filter {
                (selectedSubjectFilter == null || it.subjectId == selectedSubjectFilter) &&
                        (selectedPriorityFilter == null || it.priority == selectedPriorityFilter)
            }

            if (displayList.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = StatusSubmittedColor
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "All caught up!",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "No pending assignments for the selected filters.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(displayList) { assignment ->
                    DashboardAssignmentCard(
                        assignment = assignment,
                        onStatusToggle = {
                            val nextStatus = when (assignment.status) {
                                AssignmentStatus.PENDING -> AssignmentStatus.IN_PROGRESS
                                AssignmentStatus.IN_PROGRESS -> AssignmentStatus.SUBMITTED
                                AssignmentStatus.SUBMITTED -> AssignmentStatus.PENDING
                            }
                            viewModel.updateAssignmentStatus(assignment.id, nextStatus)
                        },
                        onEdit = { editingAssignment = assignment }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddEditAssignmentDialog(
            assignment = null,
            enrolledSubjects = enrolledSubjects,
            onDismiss = { showAddDialog = false },
            onSave = { newAssignment ->
                viewModel.saveAssignment(newAssignment)
                showAddDialog = false
            }
        )
    }

    editingAssignment?.let { assignment ->
        AddEditAssignmentDialog(
            assignment = assignment,
            enrolledSubjects = enrolledSubjects,
            onDismiss = { editingAssignment = null },
            onSave = { updated ->
                viewModel.saveAssignment(updated)
                editingAssignment = null
            }
        )
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun DashboardAssignmentCard(
    assignment: Assignment,
    onStatusToggle: () -> Unit,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("dashboard_assignment_${assignment.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onEdit() },
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Priority Pill
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
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Due Date
                    Text(
                        text = assignment.dueDate,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = assignment.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = assignment.subjectName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Status chip button
            Button(
                onClick = onStatusToggle,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = when (assignment.status) {
                        AssignmentStatus.SUBMITTED -> StatusSubmittedColor
                        AssignmentStatus.IN_PROGRESS -> MaterialTheme.colorScheme.secondary
                        AssignmentStatus.PENDING -> MaterialTheme.colorScheme.outline
                    }
                ),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier.testTag("toggle_status_${assignment.id}")
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
