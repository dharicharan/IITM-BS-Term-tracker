package com.example.ui.assignments

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.Assignment
import com.example.data.model.AssignmentStatus
import com.example.data.model.Priority
import com.example.ui.theme.PriorityCriticalColor
import com.example.ui.theme.PriorityHighColor
import com.example.ui.theme.PriorityLowColor
import com.example.ui.theme.PriorityMediumColor
import com.example.ui.theme.StatusInProgressColor
import com.example.ui.theme.StatusPendingColor
import com.example.ui.theme.StatusSubmittedColor
import com.example.ui.viewmodel.TermTrackerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignmentsScreen(
    viewModel: TermTrackerViewModel
) {
    val filteredAssignments by viewModel.filteredAssignments.collectAsStateWithLifecycle()
    val enrolledSubjects by viewModel.enrolledSubjects.collectAsStateWithLifecycle()
    val selectedSubjectFilter by viewModel.selectedSubjectFilter.collectAsStateWithLifecycle()
    val selectedPriorityFilter by viewModel.selectedPriorityFilter.collectAsStateWithLifecycle()
    val selectedStatusFilter by viewModel.selectedStatusFilter.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    var showAddDialog by remember { mutableStateOf(false) }
    var editingAssignment by remember { mutableStateOf<Assignment?>(null) }
    var assignmentToDelete by remember { mutableStateOf<Assignment?>(null) }
    var quickScoreAssignment by remember { mutableStateOf<Assignment?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.testTag("add_assignment_list_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Assignment")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = { Text("Search assignments, subjects, notes...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear search")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("assignment_search_field"),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Priority Filter Row
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedPriorityFilter == null,
                        onClick = { viewModel.setPriorityFilter(null) },
                        label = { Text("All Priorities") }
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

            // Status Filter Row
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedStatusFilter == null,
                        onClick = { viewModel.setStatusFilter(null) },
                        label = { Text("All Status") }
                    )
                }
                items(AssignmentStatus.values()) { s ->
                    FilterChip(
                        selected = selectedStatusFilter == s,
                        onClick = {
                            viewModel.setStatusFilter(if (selectedStatusFilter == s) null else s)
                        },
                        label = { Text(s.displayName) }
                    )
                }
            }

            // Subject Filter Row
            if (enrolledSubjects.isNotEmpty()) {
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

            // Results count
            Text(
                text = "${filteredAssignments.size} assignments found",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Assignments List
            if (filteredAssignments.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "No assignments found",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Try adjusting your search or filters",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredAssignments, key = { it.id }) { assignment ->
                        AssignmentListItem(
                            assignment = assignment,
                            onStatusClick = {
                                val next = when (assignment.status) {
                                    AssignmentStatus.PENDING -> AssignmentStatus.IN_PROGRESS
                                    AssignmentStatus.IN_PROGRESS -> AssignmentStatus.SUBMITTED
                                    AssignmentStatus.SUBMITTED -> AssignmentStatus.PENDING
                                }
                                viewModel.updateAssignmentStatus(assignment.id, next)
                            },
                            onScoreClick = { quickScoreAssignment = assignment },
                            onEdit = { editingAssignment = assignment },
                            onDelete = { assignmentToDelete = assignment }
                        )
                    }
                }
            }
        }
    }

    // Add dialog
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

    // Edit dialog
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

    // Delete confirmation dialog
    assignmentToDelete?.let { toDelete ->
        AlertDialog(
            onDismissRequest = { assignmentToDelete = null },
            title = { Text("Delete Assignment") },
            text = { Text("Are you sure you want to remove '${toDelete.title}'?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteAssignment(toDelete.id)
                        assignmentToDelete = null
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { assignmentToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Quick score editor dialog
    quickScoreAssignment?.let { assignment ->
        var scoreInput by remember { mutableStateOf(assignment.score?.toString() ?: "") }
        AlertDialog(
            onDismissRequest = { quickScoreAssignment = null },
            title = { Text("Update Score") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(assignment.title, style = MaterialTheme.typography.bodyMedium)
                    OutlinedTextField(
                        value = scoreInput,
                        onValueChange = { scoreInput = it },
                        label = { Text("Score (out of ${assignment.maxScore})") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val score = scoreInput.toFloatOrNull()
                        viewModel.updateAssignmentScore(assignment.id, score)
                        if (score != null) {
                            viewModel.updateAssignmentStatus(assignment.id, AssignmentStatus.SUBMITTED)
                        }
                        quickScoreAssignment = null
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { quickScoreAssignment = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun AssignmentListItem(
    assignment: Assignment,
    onStatusClick: () -> Unit,
    onScoreClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("assignment_item_${assignment.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header Tags
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Priority Badge
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
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Assignment Type Tag
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = assignment.type.displayName,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                // Status chip
                Button(
                    onClick = onStatusClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when (assignment.status) {
                            AssignmentStatus.SUBMITTED -> StatusSubmittedColor
                            AssignmentStatus.IN_PROGRESS -> StatusInProgressColor
                            AssignmentStatus.PENDING -> StatusPendingColor
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

            // Title & Subject
            Text(
                text = assignment.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = assignment.subjectName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (assignment.notes.isNotBlank()) {
                Text(
                    text = assignment.notes,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Footer: Due Date, Score chip, Edit/Delete
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Default.CalendarToday,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Due: ${assignment.dueDate}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Score chip
                    Surface(
                        modifier = Modifier.clickable { onScoreClick() },
                        color = if (assignment.score != null) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Grade,
                                contentDescription = "Score",
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (assignment.score != null) "${assignment.score}/${assignment.maxScore.toInt()}" else "Add Score",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(16.dp))
                    }

                    IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}
