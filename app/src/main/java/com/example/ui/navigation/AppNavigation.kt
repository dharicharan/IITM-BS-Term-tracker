package com.example.ui.navigation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.core.content.ContextCompat
import com.example.data.model.Subject
import com.example.notifications.ReminderManager
import com.example.ui.assignments.AssignmentsScreen
import com.example.ui.calendar.CalendarScreen
import com.example.ui.dashboard.DashboardScreen
import com.example.ui.gradecalc.GradeCalculatorScreen
import com.example.ui.subjects.SubjectsScreen
import com.example.ui.viewmodel.TermTrackerViewModel

enum class Screen(val title: String, val icon: ImageVector, val tag: String) {
    DASHBOARD("Dashboard", Icons.Default.Dashboard, "nav_dashboard"),
    ASSIGNMENTS("Assignments", Icons.Default.Assignment, "nav_assignments"),
    CALENDAR("Calendar", Icons.Default.CalendarMonth, "nav_calendar"),
    SUBJECTS("Subjects", Icons.Default.School, "nav_subjects"),
    CALCULATOR("Calculator", Icons.Default.Calculate, "nav_calculator")
}

@Composable
fun AppNavigation(viewModel: TermTrackerViewModel) {
    val context = LocalContext.current
    var currentScreen by remember { mutableStateOf(Screen.DASHBOARD) }
    var calculatorSubject by remember { mutableStateOf<Subject?>(null) }

    // Request notification permissions gracefully on Android 13+
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            ReminderManager.createNotificationChannel(context)
            viewModel.triggerCheckReminders(context)
        }
    }

    LaunchedEffect(Unit) {
        ReminderManager.createNotificationChannel(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        viewModel.triggerCheckReminders(context)
    }

    // Handle system back navigation to return to Dashboard
    if (currentScreen != Screen.DASHBOARD) {
        BackHandler {
            currentScreen = Screen.DASHBOARD
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier.testTag("bottom_nav_bar"),
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Screen.values().forEach { screen ->
                    val isSelected = currentScreen == screen
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentScreen = screen },
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.title
                            )
                        },
                        label = {
                            Text(
                                text = screen.title,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        modifier = Modifier.testTag(screen.tag)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                Screen.DASHBOARD -> DashboardScreen(
                    viewModel = viewModel,
                    onNavigateToSubjects = { currentScreen = Screen.SUBJECTS },
                    onNavigateToGradeCalc = { currentScreen = Screen.CALCULATOR }
                )
                Screen.ASSIGNMENTS -> AssignmentsScreen(
                    viewModel = viewModel
                )
                Screen.CALENDAR -> CalendarScreen(
                    viewModel = viewModel
                )
                Screen.SUBJECTS -> SubjectsScreen(
                    viewModel = viewModel,
                    onNavigateToCalculatorForSubject = { subj ->
                        calculatorSubject = subj
                        viewModel.selectSubjectForCalculator(subj)
                        currentScreen = Screen.CALCULATOR
                    }
                )
                Screen.CALCULATOR -> GradeCalculatorScreen(
                    viewModel = viewModel,
                    preSelectedSubject = calculatorSubject
                )
            }
        }
    }
}
