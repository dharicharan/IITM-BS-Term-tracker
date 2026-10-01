package com.example

import com.example.data.initial.InitialCoursesData
import com.example.data.model.AssignmentStatus
import com.example.data.model.GradeCalculator
import com.example.data.model.GradeInputs
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testFoundationStandardGradeFormula() {
        val math1 = InitialCoursesData.defaultSubjects.first { it.id == "math1" }
        val inputs = GradeInputs(
            quiz1 = 70f,
            quiz2 = 85f,
            finalExam = 80f
        )
        val result = GradeCalculator.calculate(math1, inputs)
        assertEquals(79.0f, result.totalScore, 0.01f)
        assertEquals("B", result.letterGrade)
        assertTrue(result.isPass)
        assertTrue(result.isEligibleForFinal)
    }

    @Test
    fun testSep2026MlfGradeFormula() {
        val mlf = InitialCoursesData.defaultSubjects.first { it.id == "mlf" }
        val inputs = GradeInputs(
            quiz1 = 80f,
            quiz2 = 90f,
            finalExam = 80f,
            bonus = 3f
        )
        val result = GradeCalculator.calculate(mlf, inputs)
        assertEquals(86.0f, result.totalScore, 0.01f)
        assertEquals("A", result.letterGrade)
        assertTrue(result.isPass)
    }

    @Test
    fun testSep2026MlpGradeFormula() {
        val mlp = InitialCoursesData.defaultSubjects.first { it.id == "mlp" }
        val inputs = GradeInputs(
            gaa = 90f,
            finalExam = 70f,
            oppe1 = 80f,
            oppe2 = 85f,
            projectOrLab = 90f
        )
        val result = GradeCalculator.calculate(mlp, inputs)
        assertEquals(79.0f, result.totalScore, 0.01f)
        assertEquals("B", result.letterGrade)
        assertTrue(result.isPass)
    }

    @Test
    fun testFinancialForensicsCourseAndFormula() {
        val ff = InitialCoursesData.defaultSubjects.find { it.id == "financial_forensics" }
        assertNotNull("Financial Forensics must exist in defaultSubjects", ff)
        assertEquals("MS4003", ff!!.code)
        assertEquals("Financial Forensics", ff.name)

        // Formula: T = 0.1GAA + 0.25Qz1 + 0.30GP1 + 0.35F
        // With GAA=80, Q1=80, GP1=90, F=80:
        // T = 0.1*80 + 0.25*80 + 0.30*90 + 0.35*80 = 8 + 20 + 27 + 28 = 83.0 -> Grade A
        val inputs = GradeInputs(
            gaa = 80f,
            quiz1 = 80f,
            projectOrLab = 90f, // GP1
            finalExam = 80f
        )
        val result = GradeCalculator.calculate(ff, inputs)
        assertEquals(83.0f, result.totalScore, 0.01f)
        assertEquals("A", result.letterGrade)
        assertTrue(result.isPass)

        val assignments = InitialCoursesData.generateDefaultAssignmentsForSubject(ff)
        assertTrue(assignments.any { it.title.contains("Remote Group Project 1 (GP1)") })
        assertTrue(assignments.any { it.title.startsWith("Quiz 1") })
        assertTrue(assignments.any { it.title.contains("End Term") })
    }

    @Test
    fun testSep2026AssignmentsGeneration() {
        val python = InitialCoursesData.defaultSubjects.first { it.id == "python" }
        val assignments = InitialCoursesData.generateDefaultAssignmentsForSubject(python)
        assertTrue(assignments.size >= 16)
        val quiz1 = assignments.first { it.title.startsWith("Quiz 1") }
        assertEquals("2026-11-15", quiz1.dueDate)

        val endTerm = assignments.first { it.title.contains("End Term") }
        assertEquals("2027-01-10", endTerm.dueDate)
    }

    @Test
    fun testSubjectCompletionCalculation() {
        val math1 = InitialCoursesData.defaultSubjects.first { it.id == "math1" }
        val assignments = InitialCoursesData.generateDefaultAssignmentsForSubject(math1)
        val updated = assignments.mapIndexed { index, a ->
            if (index < 4) a.copy(status = AssignmentStatus.SUBMITTED) else a
        }
        val total = updated.size
        val completed = updated.count { it.status == AssignmentStatus.SUBMITTED }
        val pct = (completed.toFloat() / total.toFloat()) * 100f
        assertEquals(4, completed)
        assertTrue(pct > 0f)
    }
}
