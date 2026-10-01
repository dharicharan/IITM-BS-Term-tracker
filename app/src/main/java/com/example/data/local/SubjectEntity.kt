package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.CourseLevel
import com.example.data.model.Subject
import com.example.data.model.SubjectFormulaType

@Entity(tableName = "subjects")
data class SubjectEntity(
    @PrimaryKey val id: String,
    val name: String,
    val code: String,
    val level: CourseLevel,
    val colorHex: Long,
    val isEnrolled: Boolean,
    val formulaType: SubjectFormulaType,
    val formulaDescription: String,
    val eligibilityCriteria: String,
    val hasOppe: Boolean,
    val hasQuiz1: Boolean,
    val hasQuiz2: Boolean,
    val hasEndTerm: Boolean,
    val credits: Int
) {
    fun toDomain(): Subject = Subject(
        id = id,
        name = name,
        code = code,
        level = level,
        colorHex = colorHex,
        isEnrolled = isEnrolled,
        formulaType = formulaType,
        formulaDescription = formulaDescription,
        eligibilityCriteria = eligibilityCriteria,
        hasOppe = hasOppe,
        hasQuiz1 = hasQuiz1,
        hasQuiz2 = hasQuiz2,
        hasEndTerm = hasEndTerm,
        credits = credits
    )

    companion object {
        fun fromDomain(subject: Subject): SubjectEntity = SubjectEntity(
            id = subject.id,
            name = subject.name,
            code = subject.code,
            level = subject.level,
            colorHex = subject.colorHex,
            isEnrolled = subject.isEnrolled,
            formulaType = subject.formulaType,
            formulaDescription = subject.formulaDescription,
            eligibilityCriteria = subject.eligibilityCriteria,
            hasOppe = subject.hasOppe,
            hasQuiz1 = subject.hasQuiz1,
            hasQuiz2 = subject.hasQuiz2,
            hasEndTerm = subject.hasEndTerm,
            credits = subject.credits
        )
    }
}
