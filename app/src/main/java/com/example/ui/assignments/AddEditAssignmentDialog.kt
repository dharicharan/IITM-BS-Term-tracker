package com.example.ui.assignments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.data.model.Assignment
import com.example.data.model.AssignmentStatus
import com.example.data.model.AssignmentType
import com.example.data.model.Priority
import com.example.data.model.Subject
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditAssignmentDialog(
    assignment: Assignment?,
    enrolledSubjects: List<Subject>,
    onDismiss: () -> Unit,
    onSave: (Assignment) -> Unit
) {
    var title by remember { mutableStateOf(assignment?.title ?: "") }
    var selectedSubject by remember {
        mutableStateOf(
            enrolledSubjects.find { it.id == assignment?.subjectId } ?: enrolledSubjects.firstOrNull()
        )
    }
    var type by remember { mutableStateOf(assignment?.type ?: AssignmentType.WEEKLY_GA) }
    var dueDate by remember { mutableStateOf(assignment?.dueDate ?: "2026-10-18") }
    var priority by remember { mutableStateOf(assignment?.priority ?: Priority.MEDIUM) }
    var status by remember { mutableStateOf(assignment?.status ?: AssignmentStatus.PENDING) }
    var scoreText by remember { mutableStateOf(assignment?.score?.toString() ?: "") }
    var maxScoreText by remember { mutableStateOf((assignment?.maxScore ?: 100f).toString()) }
    var notes by remember { mutableStateOf(assignment?.notes ?: "") }
    var reminderEnabled by remember { mutableStateOf(assignment?.reminderEnabled ?: true) }

    var subjectMenuExpanded by remember { mutableStateOf(false) }
    var typeMenuExpanded by remember { mutableStateOf(false) }

    val dateFormat = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("Asia/Kolkata")
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (assignment == null) "Add New Assignment" else "Edit Assignment",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Title
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Assignment Title *") },
                    placeholder = { Text("e.g. Week 4 Graded Assignment") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("assignment_title_input"),
                    singleLine = true
                )

                // Subject Picker
                if (enrolledSubjects.isNotEmpty()) {
                    ExposedDropdownMenuBox(
                        expanded = subjectMenuExpanded,
                        onExpandedChange = { subjectMenuExpanded = !subjectMenuExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedSubject?.name ?: "Select Subject",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Subject *") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = subjectMenuExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                                .testTag("subject_dropdown")
                        )
                        ExposedDropdownMenu(
                            expanded = subjectMenuExpanded,
                            onDismissRequest = { subjectMenuExpanded = false }
                        ) {
                            enrolledSubjects.forEach { subj ->
                                DropdownMenuItem(
                                    text = { Text("${subj.code} - ${subj.name}") },
                                    onClick = {
                                        selectedSubject = subj
                                        subjectMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Type Picker
                ExposedDropdownMenuBox(
                    expanded = typeMenuExpanded,
                    onExpandedChange = { typeMenuExpanded = !typeMenuExpanded }
                ) {
                    OutlinedTextField(
                        value = type.displayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Assignment Type") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeMenuExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = typeMenuExpanded,
                        onDismissRequest = { typeMenuExpanded = false }
                    ) {
                        AssignmentType.values().forEach { t ->
                            DropdownMenuItem(
                                text = { Text(t.displayName) },
                                onClick = {
                                    type = t
                                    typeMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                // Due Date
                OutlinedTextField(
                    value = dueDate,
                    onValueChange = { dueDate = it },
                    label = { Text("Due Date (YYYY-MM-DD) *") },
                    leadingIcon = {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Due Date")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("due_date_input"),
                    singleLine = true
                )

                // Priority Selection
                Text(
                    text = "Priority Level",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Priority.values().forEach { p ->
                        FilterChip(
                            selected = priority == p,
                            onClick = { priority = p },
                            label = { Text(p.displayName) },
                            modifier = Modifier.testTag("priority_${p.name.lowercase()}")
                        )
                    }
                }

                // Status Selection
                Text(
                    text = "Status",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    AssignmentStatus.values().forEach { s ->
                        FilterChip(
                            selected = status == s,
                            onClick = { status = s },
                            label = { Text(s.displayName) }
                        )
                    }
                }

                // Score Inputs
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = scoreText,
                        onValueChange = { scoreText = it },
                        label = { Text("Score (optional)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("score_input"),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = maxScoreText,
                        onValueChange = { maxScoreText = it },
                        label = { Text("Max Score") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                // Notes
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes / Guidelines") },
                    placeholder = { Text("e.g. Needs OPPE SCT pass or Discourse participation") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                // Reminder Switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "Reminder",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Automated Reminders", style = MaterialTheme.typography.bodyMedium)
                    }
                    Switch(
                        checked = reminderEnabled,
                        onCheckedChange = { reminderEnabled = it }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isBlank() || selectedSubject == null) return@TextButton

                    val timestamp = try {
                        dateFormat.parse(dueDate)?.time ?: System.currentTimeMillis()
                    } catch (_: Exception) {
                        System.currentTimeMillis()
                    }

                    val scoreVal = scoreText.toFloatOrNull()
                    val maxScoreVal = maxScoreText.toFloatOrNull() ?: 100f

                    val updated = (assignment ?: Assignment(
                        subjectId = selectedSubject!!.id,
                        subjectName = selectedSubject!!.name,
                        title = title,
                        type = type,
                        dueDate = dueDate,
                        dueTimestamp = timestamp
                    )).copy(
                        title = title,
                        subjectId = selectedSubject!!.id,
                        subjectName = selectedSubject!!.name,
                        type = type,
                        dueDate = dueDate,
                        dueTimestamp = timestamp,
                        priority = priority,
                        status = status,
                        score = scoreVal,
                        maxScore = maxScoreVal,
                        notes = notes,
                        reminderEnabled = reminderEnabled
                    )

                    onSave(updated)
                },
                modifier = Modifier.testTag("save_assignment_button")
            ) {
                Text("Save", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
