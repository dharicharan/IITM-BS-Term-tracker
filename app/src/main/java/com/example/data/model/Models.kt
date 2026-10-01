package com.example.data.model

data class Subject(
    val id: String,
    val name: String,
    val code: String,
    val level: CourseLevel,
    val colorHex: Long,
    val isEnrolled: Boolean = false,
    val formulaType: SubjectFormulaType = SubjectFormulaType.FOUNDATION_STANDARD,
    val formulaDescription: String = "",
    val eligibilityCriteria: String = "",
    val hasOppe: Boolean = false,
    val hasQuiz1: Boolean = true,
    val hasQuiz2: Boolean = true,
    val hasEndTerm: Boolean = true,
    val credits: Int = 4
)

data class Assignment(
    val id: Long = 0,
    val subjectId: String,
    val subjectName: String,
    val title: String,
    val weekNumber: Int? = null,
    val type: AssignmentType,
    val dueDate: String, // e.g. "2026-06-24"
    val dueTimestamp: Long,
    val priority: Priority = Priority.MEDIUM,
    val status: AssignmentStatus = AssignmentStatus.PENDING,
    val maxScore: Float = 100f,
    val score: Float? = null,
    val weight: Float = 0f,
    val notes: String = "",
    val reminderEnabled: Boolean = true,
    val reminderHoursBefore: Int = 24
)
