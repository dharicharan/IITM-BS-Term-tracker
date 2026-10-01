package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.Assignment
import com.example.data.model.AssignmentStatus
import com.example.data.model.AssignmentType
import com.example.data.model.Priority

@Entity(tableName = "assignments")
data class AssignmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val subjectId: String,
    val subjectName: String,
    val title: String,
    val weekNumber: Int?,
    val type: AssignmentType,
    val dueDate: String,
    val dueTimestamp: Long,
    val priority: Priority,
    val status: AssignmentStatus,
    val maxScore: Float,
    val score: Float?,
    val weight: Float,
    val notes: String,
    val reminderEnabled: Boolean,
    val reminderHoursBefore: Int
) {
    fun toDomain(): Assignment = Assignment(
        id = id,
        subjectId = subjectId,
        subjectName = subjectName,
        title = title,
        weekNumber = weekNumber,
        type = type,
        dueDate = dueDate,
        dueTimestamp = dueTimestamp,
        priority = priority,
        status = status,
        maxScore = maxScore,
        score = score,
        weight = weight,
        notes = notes,
        reminderEnabled = reminderEnabled,
        reminderHoursBefore = reminderHoursBefore
    )

    companion object {
        fun fromDomain(a: Assignment): AssignmentEntity = AssignmentEntity(
            id = a.id,
            subjectId = a.subjectId,
            subjectName = a.subjectName,
            title = a.title,
            weekNumber = a.weekNumber,
            type = a.type,
            dueDate = a.dueDate,
            dueTimestamp = a.dueTimestamp,
            priority = a.priority,
            status = a.status,
            maxScore = a.maxScore,
            score = a.score,
            weight = a.weight,
            notes = a.notes,
            reminderEnabled = a.reminderEnabled,
            reminderHoursBefore = a.reminderHoursBefore
        )
    }
}
