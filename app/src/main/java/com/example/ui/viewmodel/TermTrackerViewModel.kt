package com.example.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Assignment
import com.example.data.model.AssignmentStatus
import com.example.data.model.AssignmentType
import com.example.data.model.GradeCalculator
import com.example.data.model.GradeInputs
import com.example.data.model.GradeResult
import com.example.data.model.Priority
import com.example.data.model.Subject
import com.example.data.repository.AssignmentRepository
import com.example.notifications.ReminderManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TermTrackerViewModel(
    private val repository: AssignmentRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            repository.ensureInitialData()
        }
    }

    val allSubjects: StateFlow<List<Subject>> = repository.allSubjects.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val enrolledSubjects: StateFlow<List<Subject>> = repository.enrolledSubjects.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allAssignments: StateFlow<List<Assignment>> = repository.allAssignments.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val activeAssignments: StateFlow<List<Assignment>> = repository.activeAssignments.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Filters
    private val _selectedSubjectFilter = MutableStateFlow<String?>(null)
    val selectedSubjectFilter: StateFlow<String?> = _selectedSubjectFilter.asStateFlow()

    private val _selectedPriorityFilter = MutableStateFlow<Priority?>(null)
    val selectedPriorityFilter: StateFlow<Priority?> = _selectedPriorityFilter.asStateFlow()

    private val _selectedStatusFilter = MutableStateFlow<AssignmentStatus?>(null)
    val selectedStatusFilter: StateFlow<AssignmentStatus?> = _selectedStatusFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Filtered assignments stream
    val filteredAssignments: StateFlow<List<Assignment>> = combine(
        allAssignments,
        selectedSubjectFilter,
        selectedPriorityFilter,
        selectedStatusFilter,
        searchQuery
    ) { assignments, subjectId, priority, status, query ->
        assignments.filter { item ->
            val matchSubject = subjectId == null || item.subjectId == subjectId
            val matchPriority = priority == null || item.priority == priority
            val matchStatus = status == null || item.status == status
            val matchQuery = query.isBlank() ||
                    item.title.contains(query, ignoreCase = true) ||
                    item.subjectName.contains(query, ignoreCase = true) ||
                    item.notes.contains(query, ignoreCase = true)
            matchSubject && matchPriority && matchStatus && matchQuery
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Calendar selected date (default to first assignment deadline)
    private val todayString = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    private val _selectedCalendarDate = MutableStateFlow("2026-10-11") // Default to Week 1 Sep 2026 Term
    val selectedCalendarDate: StateFlow<String> = _selectedCalendarDate.asStateFlow()

    // Grade Calculator state
    private val _calcSubject = MutableStateFlow<Subject?>(null)
    val calcSubject: StateFlow<Subject?> = _calcSubject.asStateFlow()

    private val _calcInputs = MutableStateFlow(GradeInputs())
    val calcInputs: StateFlow<GradeInputs> = _calcInputs.asStateFlow()

    private val _calcResult = MutableStateFlow<GradeResult?>(null)
    val calcResult: StateFlow<GradeResult?> = _calcResult.asStateFlow()

    // Filter setters
    fun setSubjectFilter(subjectId: String?) {
        _selectedSubjectFilter.value = subjectId
    }

    fun setPriorityFilter(priority: Priority?) {
        _selectedPriorityFilter.value = priority
    }

    fun setStatusFilter(status: AssignmentStatus?) {
        _selectedStatusFilter.value = status
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCalendarDate(dateStr: String) {
        _selectedCalendarDate.value = dateStr
    }

    // Assignment Actions
    fun updateAssignmentStatus(id: Long, status: AssignmentStatus) {
        viewModelScope.launch {
            repository.updateAssignmentStatus(id, status)
        }
    }

    fun updateAssignmentScore(id: Long, score: Float?) {
        viewModelScope.launch {
            repository.updateAssignmentScore(id, score)
        }
    }

    fun saveAssignment(assignment: Assignment) {
        viewModelScope.launch {
            if (assignment.id == 0L) {
                repository.insertAssignment(assignment)
            } else {
                repository.updateAssignment(assignment)
            }
        }
    }

    fun deleteAssignment(id: Long) {
        viewModelScope.launch {
            repository.deleteAssignment(id)
        }
    }

    // Subject Enrollment Actions
    fun toggleSubjectEnrollment(subject: Subject) {
        viewModelScope.launch {
            val newStatus = !subject.isEnrolled
            repository.toggleSubjectEnrollment(subject, newStatus)
        }
    }

    // Notifications
    fun triggerCheckReminders(context: Context) {
        viewModelScope.launch {
            val active = activeAssignments.value
            ReminderManager.checkAndTriggerAutomatedReminders(context, active)
        }
    }

    fun sendTestNotification(context: Context) {
        ReminderManager.sendTestNotification(context)
    }

    // Grade Calculator Actions
    fun selectSubjectForCalculator(subject: Subject) {
        _calcSubject.value = subject
        // Automatically populate inputs based on submitted assignments for this subject
        autoPopulateCalculatorInputs(subject)
    }

    fun updateCalculatorInputs(newInputs: GradeInputs) {
        _calcInputs.value = newInputs
        val subject = _calcSubject.value ?: return
        _calcResult.value = GradeCalculator.calculate(subject, newInputs)
    }

    private fun autoPopulateCalculatorInputs(subject: Subject) {
        viewModelScope.launch {
            val assignments = repository.getAssignmentsForSubjectSync(subject.id)
            val submittedGAs = assignments
                .filter { it.type == AssignmentType.WEEKLY_GA && it.score != null }
                .mapNotNull { it.score }

            val gaaAverage = if (submittedGAs.isNotEmpty()) {
                // Best 5 of first 7 or top scores
                val sorted = submittedGAs.sortedDescending()
                val bestCount = minOf(5, sorted.size)
                sorted.take(bestCount).average().toFloat()
            } else 85f

            val quiz1Score = assignments.find { it.type == AssignmentType.QUIZ && it.title.contains("Quiz 1") }?.score ?: 75f
            val quiz2Score = assignments.find { it.type == AssignmentType.QUIZ && it.title.contains("Quiz 2") }?.score ?: 80f
            val endTermScore = assignments.find { it.type == AssignmentType.QUIZ && it.title.contains("End Term") }?.score ?: 70f
            val oppe1Score = assignments.find { it.type == AssignmentType.OPPE && it.title.contains("OPPE 1") }?.score ?: 80f
            val oppe2Score = assignments.find { it.type == AssignmentType.OPPE && it.title.contains("OPPE 2") }?.score ?: 85f

            val initialInputs = GradeInputs(
                gaa = gaaAverage,
                quiz1 = quiz1Score,
                quiz2 = quiz2Score,
                finalExam = endTermScore,
                oppe1 = oppe1Score,
                oppe2 = oppe2Score,
                projectOrLab = 85f,
                bonus = 2f,
                sctCompleted = true,
                attendedQuiz = true,
                attendedFinal = true
            )
            _calcInputs.value = initialInputs
            _calcResult.value = GradeCalculator.calculate(subject, initialInputs)
        }
    }
}
