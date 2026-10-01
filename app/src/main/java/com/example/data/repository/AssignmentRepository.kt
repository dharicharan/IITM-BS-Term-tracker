package com.example.data.repository

import com.example.data.initial.InitialCoursesData
import com.example.data.local.AssignmentDao
import com.example.data.local.AssignmentEntity
import com.example.data.local.SubjectDao
import com.example.data.local.SubjectEntity
import com.example.data.model.Assignment
import com.example.data.model.AssignmentStatus
import com.example.data.model.Subject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AssignmentRepository(
    private val subjectDao: SubjectDao,
    private val assignmentDao: AssignmentDao
) {

    val allSubjects: Flow<List<Subject>> = subjectDao.getAllSubjects().map { entities ->
        entities.map { it.toDomain() }
    }

    val enrolledSubjects: Flow<List<Subject>> = subjectDao.getEnrolledSubjects().map { entities ->
        entities.map { it.toDomain() }
    }

    val allAssignments: Flow<List<Assignment>> = assignmentDao.getAllAssignments().map { entities ->
        entities.map { it.toDomain() }
    }

    val activeAssignments: Flow<List<Assignment>> = assignmentDao.getActiveAssignments().map { entities ->
        entities.map { it.toDomain() }
    }

    fun getAssignmentsForSubject(subjectId: String): Flow<List<Assignment>> =
        assignmentDao.getAssignmentsForSubject(subjectId).map { entities ->
            entities.map { it.toDomain() }
        }

    suspend fun getAssignmentsForSubjectSync(subjectId: String): List<Assignment> =
        assignmentDao.getAssignmentsForSubjectSync(subjectId).map { it.toDomain() }

    suspend fun toggleSubjectEnrollment(subject: Subject, isEnrolled: Boolean) {
        subjectDao.updateEnrollment(subject.id, isEnrolled)
        if (isEnrolled) {
            // Check if assignments already exist for this subject
            val existing = assignmentDao.getAssignmentsForSubjectSync(subject.id)
            if (existing.isEmpty()) {
                val newAssignments = InitialCoursesData.generateDefaultAssignmentsForSubject(subject)
                assignmentDao.insertAssignments(newAssignments.map { AssignmentEntity.fromDomain(it) })
            }
        } else {
            // Optional: keep or clear assignments. Removing keeps the dashboard clean for active term
            assignmentDao.deleteAssignmentsForSubject(subject.id)
        }
    }

    suspend fun insertAssignment(assignment: Assignment): Long {
        return assignmentDao.insertAssignment(AssignmentEntity.fromDomain(assignment))
    }

    suspend fun updateAssignment(assignment: Assignment) {
        assignmentDao.updateAssignment(AssignmentEntity.fromDomain(assignment))
    }

    suspend fun updateAssignmentStatus(id: Long, status: AssignmentStatus) {
        assignmentDao.updateStatus(id, status)
    }

    suspend fun updateAssignmentScore(id: Long, score: Float?) {
        assignmentDao.updateScore(id, score)
    }

    suspend fun deleteAssignment(id: Long) {
        assignmentDao.deleteAssignmentById(id)
    }

    suspend fun getUpcomingDueAssignments(thresholdTimestamp: Long): List<Assignment> {
        return assignmentDao.getUpcomingDueAssignments(thresholdTimestamp).map { it.toDomain() }
    }

    suspend fun ensureInitialData() {
        val subjectCount = subjectDao.getCount()
        val defaultSubjects = InitialCoursesData.defaultSubjects
        if (subjectCount < defaultSubjects.size) {
            for (subj in defaultSubjects) {
                val existing = subjectDao.getSubjectById(subj.id)
                if (existing == null) {
                    subjectDao.insertSubject(SubjectEntity.fromDomain(subj))
                }
            }
            if (subjectCount == 0) {
                val enrolled = defaultSubjects.filter { it.isEnrolled }
                for (subj in enrolled) {
                    val assignments = InitialCoursesData.generateDefaultAssignmentsForSubject(subj)
                    assignmentDao.insertAssignments(assignments.map { AssignmentEntity.fromDomain(it) })
                }
            }
        }
    }
}
