package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AssignmentStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface AssignmentDao {
    @Query("SELECT * FROM assignments ORDER BY dueTimestamp ASC, priority DESC")
    fun getAllAssignments(): Flow<List<AssignmentEntity>>

    @Query("SELECT * FROM assignments WHERE subjectId = :subjectId ORDER BY dueTimestamp ASC")
    fun getAssignmentsForSubject(subjectId: String): Flow<List<AssignmentEntity>>

    @Query("SELECT * FROM assignments WHERE subjectId = :subjectId ORDER BY dueTimestamp ASC")
    suspend fun getAssignmentsForSubjectSync(subjectId: String): List<AssignmentEntity>

    @Query("SELECT * FROM assignments WHERE id = :id LIMIT 1")
    suspend fun getAssignmentById(id: Long): AssignmentEntity?

    @Query("SELECT * FROM assignments WHERE status != 'SUBMITTED' ORDER BY dueTimestamp ASC")
    fun getActiveAssignments(): Flow<List<AssignmentEntity>>

    @Query("SELECT * FROM assignments WHERE status != 'SUBMITTED' AND dueTimestamp <= :thresholdTimestamp ORDER BY dueTimestamp ASC")
    suspend fun getUpcomingDueAssignments(thresholdTimestamp: Long): List<AssignmentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignment(assignment: AssignmentEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignments(assignments: List<AssignmentEntity>)

    @Update
    suspend fun updateAssignment(assignment: AssignmentEntity)

    @Query("UPDATE assignments SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Long, status: AssignmentStatus)

    @Query("UPDATE assignments SET score = :score WHERE id = :id")
    suspend fun updateScore(id: Long, score: Float?)

    @Delete
    suspend fun deleteAssignment(assignment: AssignmentEntity)

    @Query("DELETE FROM assignments WHERE id = :id")
    suspend fun deleteAssignmentById(id: Long)

    @Query("DELETE FROM assignments WHERE subjectId = :subjectId")
    suspend fun deleteAssignmentsForSubject(subjectId: String)

    @Query("SELECT COUNT(*) FROM assignments")
    suspend fun getCount(): Int
}
