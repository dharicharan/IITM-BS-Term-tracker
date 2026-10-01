package com.example.ui.gradecalc

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.GradeInputs
import com.example.data.model.Subject
import com.example.ui.theme.PriorityCriticalColor
import com.example.ui.theme.PriorityHighColor
import com.example.ui.theme.StatusSubmittedColor
import com.example.ui.viewmodel.TermTrackerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradeCalculatorScreen(
    viewModel: TermTrackerViewModel,
    preSelectedSubject: Subject? = null
) {
    val enrolledSubjects by viewModel.enrolledSubjects.collectAsStateWithLifecycle()
    val allSubjects by viewModel.allSubjects.collectAsStateWithLifecycle()
    val calcSubject by viewModel.calcSubject.collectAsStateWithLifecycle()
    val calcInputs by viewModel.calcInputs.collectAsStateWithLifecycle()
    val calcResult by viewModel.calcResult.collectAsStateWithLifecycle()

    var subjectMenuExpanded by remember { mutableStateOf(false) }

    // Target subjects: prefer enrolled subjects, otherwise all subjects
    val availableSubjects = if (enrolledSubjects.isNotEmpty()) enrolledSubjects else allSubjects

    // Initialize selection if not set
    LaunchedEffect(availableSubjects, preSelectedSubject) {
        if (preSelectedSubject != null && calcSubject?.id != preSelectedSubject.id) {
            viewModel.selectSubjectForCalculator(preSelectedSubject)
        } else if (calcSubject == null && availableSubjects.isNotEmpty()) {
            viewModel.selectSubjectForCalculator(availableSubjects.first())
        }
    }

    val activeSubject = calcSubject ?: availableSubjects.firstOrNull()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Formula Total Calculator",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Official IITM Sep 2026 Term Formulas & Eligibility",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Subject Selector
        ExposedDropdownMenuBox(
            expanded = subjectMenuExpanded,
            onExpandedChange = { subjectMenuExpanded = !subjectMenuExpanded }
        ) {
            OutlinedTextField(
                value = if (activeSubject != null) "${activeSubject.code} - ${activeSubject.name}" else "Select Subject",
                onValueChange = {},
                readOnly = true,
                label = { Text("Selected Course for Formula Calculation") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = subjectMenuExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .testTag("calc_subject_selector"),
                shape = RoundedCornerShape(12.dp)
            )

            ExposedDropdownMenu(
                expanded = subjectMenuExpanded,
                onDismissRequest = { subjectMenuExpanded = false }
            ) {
                availableSubjects.forEach { subj ->
                    DropdownMenuItem(
                        text = { Text("${subj.code} - ${subj.name}") },
                        onClick = {
                            viewModel.selectSubjectForCalculator(subj)
                            subjectMenuExpanded = false
                        }
                    )
                }
            }
        }

        if (activeSubject != null) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 70.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Total Score & Projected Grade Card
                if (calcResult != null) {
                    item {
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("calc_result_card"),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(18.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "PROJECTED TOTAL (T SCORE)",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                        )
                                        Text(
                                            text = String.format("%.1f / 100", calcResult!!.totalScore),
                                            style = MaterialTheme.typography.headlineLarge,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }

                                    // Letter Grade Pill
                                    Surface(
                                        color = when (calcResult!!.letterGrade) {
                                            "S" -> Color(0xFF047857) // Deep Emerald
                                            "A" -> Color(0xFF0284C7) // Sky Blue
                                            "B" -> Color(0xFF2563EB) // Royal Blue
                                            "C" -> Color(0xFF4F46E5) // Indigo
                                            "D", "E" -> Color(0xFFD97706) // Amber
                                            else -> PriorityCriticalColor
                                        },
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = calcResult!!.letterGrade,
                                                style = MaterialTheme.typography.headlineMedium,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = Color.White
                                            )
                                            Text(
                                                text = if (calcResult!!.isPass) "PASS" else "FAIL/INC",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }

                                // Eligibility & Passing Notes
                                Surface(
                                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            if (calcResult!!.isEligibleForFinal) Icons.Default.CheckCircle else Icons.Default.Warning,
                                            contentDescription = null,
                                            tint = if (calcResult!!.isEligibleForFinal) StatusSubmittedColor else PriorityHighColor,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Text(
                                            text = calcResult!!.eligibilityNote,
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }

                                // Step-by-Step Breakdown
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(
                                        text = "Formula Breakdown:",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = calcResult!!.breakdownText,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                        }
                    }
                }

                // Official Formula description card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Functions, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Official Formula Details",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = activeSubject.formulaDescription,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Eligibility: ${activeSubject.eligibilityCriteria}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Quick Presets Row
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                viewModel.selectSubjectForCalculator(activeSubject)
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Sync Scores", style = MaterialTheme.typography.labelSmall)
                        }

                        Button(
                            onClick = {
                                viewModel.updateCalculatorInputs(
                                    calcInputs.copy(
                                        gaa = 95f,
                                        quiz1 = 92f,
                                        quiz2 = 90f,
                                        finalExam = 88f,
                                        oppe1 = 95f,
                                        oppe2 = 95f,
                                        projectOrLab = 95f,
                                        bonus = 3f
                                    )
                                )
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Target S Grade", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }

                // Input Parameters Form Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Assessment Scores & Marks",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            // GAA (Weekly Graded Assignment average)
                            OutlinedTextField(
                                value = calcInputs.gaa.toString(),
                                onValueChange = {
                                    val v = it.toFloatOrNull() ?: 0f
                                    viewModel.updateCalculatorInputs(calcInputs.copy(gaa = v))
                                },
                                label = { Text("Graded Assignment Average (GAA / Best 5/7)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_gaa"),
                                singleLine = true
                            )

                            // Quiz 1 & Quiz 2
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (activeSubject.hasQuiz1) {
                                    OutlinedTextField(
                                        value = calcInputs.quiz1.toString(),
                                        onValueChange = {
                                            val v = it.toFloatOrNull() ?: 0f
                                            viewModel.updateCalculatorInputs(calcInputs.copy(quiz1 = v))
                                        },
                                        label = { Text("Quiz 1 Score") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("input_quiz1"),
                                        singleLine = true
                                    )
                                }

                                if (activeSubject.hasQuiz2) {
                                    OutlinedTextField(
                                        value = calcInputs.quiz2.toString(),
                                        onValueChange = {
                                            val v = it.toFloatOrNull() ?: 0f
                                            viewModel.updateCalculatorInputs(calcInputs.copy(quiz2 = v))
                                        },
                                        label = { Text("Quiz 2 Score") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("input_quiz2"),
                                        singleLine = true
                                    )
                                }
                            }

                            // Final Exam (F)
                            if (activeSubject.hasEndTerm) {
                                OutlinedTextField(
                                    value = calcInputs.finalExam.toString(),
                                    onValueChange = {
                                        val v = it.toFloatOrNull() ?: 0f
                                        viewModel.updateCalculatorInputs(calcInputs.copy(finalExam = v))
                                    },
                                    label = { Text("End Term Exam Score (Final F)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("input_final_exam"),
                                    singleLine = true
                                )
                            }

                            // OPPE 1 & OPPE 2 (if course has programming exams)
                            if (activeSubject.hasOppe) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedTextField(
                                        value = calcInputs.oppe1.toString(),
                                        onValueChange = {
                                            val v = it.toFloatOrNull() ?: 0f
                                            viewModel.updateCalculatorInputs(calcInputs.copy(oppe1 = v))
                                        },
                                        label = { Text("OPPE 1 (PE1)") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("input_oppe1"),
                                        singleLine = true
                                    )
                                    OutlinedTextField(
                                        value = calcInputs.oppe2.toString(),
                                        onValueChange = {
                                            val v = it.toFloatOrNull() ?: 0f
                                            viewModel.updateCalculatorInputs(calcInputs.copy(oppe2 = v))
                                        },
                                        label = { Text("OPPE 2 (PE2)") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("input_oppe2"),
                                        singleLine = true
                                    )
                                }
                            }

                            // Project / Lab / Kaggle
                            OutlinedTextField(
                                value = calcInputs.projectOrLab.toString(),
                                onValueChange = {
                                    val v = it.toFloatOrNull() ?: 0f
                                    viewModel.updateCalculatorInputs(calcInputs.copy(projectOrLab = v))
                                },
                                label = { Text("Project / Lab / BPTA / Assignments Score") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            // Bonus Marks
                            OutlinedTextField(
                                value = calcInputs.bonus.toString(),
                                onValueChange = {
                                    val v = it.toFloatOrNull() ?: 0f
                                    viewModel.updateCalculatorInputs(calcInputs.copy(bonus = v))
                                },
                                label = { Text("Bonus Marks (Extra Activity / Mock Tests)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            // Eligibility Toggles
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Checkbox(
                                        checked = calcInputs.attendedQuiz,
                                        onCheckedChange = {
                                            viewModel.updateCalculatorInputs(calcInputs.copy(attendedQuiz = it))
                                        }
                                    )
                                    Text("Attended at least one in-person Quiz at center", style = MaterialTheme.typography.bodySmall)
                                }

                                if (activeSubject.hasOppe) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Checkbox(
                                            checked = calcInputs.sctCompleted,
                                            onCheckedChange = {
                                                viewModel.updateCalculatorInputs(calcInputs.copy(sctCompleted = it))
                                            }
                                        )
                                        Text("Completed OPPE System Compatibility Test (SCT)", style = MaterialTheme.typography.bodySmall)
                                    }
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Checkbox(
                                        checked = calcInputs.attendedFinal,
                                        onCheckedChange = {
                                            viewModel.updateCalculatorInputs(calcInputs.copy(attendedFinal = it))
                                        }
                                    )
                                    Text("Attended End Term Exam", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
