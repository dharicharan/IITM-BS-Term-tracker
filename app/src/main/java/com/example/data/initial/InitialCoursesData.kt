package com.example.data.initial

import com.example.data.model.Assignment
import com.example.data.model.AssignmentStatus
import com.example.data.model.AssignmentType
import com.example.data.model.CourseLevel
import com.example.data.model.Priority
import com.example.data.model.Subject
import com.example.data.model.SubjectFormulaType
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object InitialCoursesData {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("Asia/Kolkata")
    }

    private fun parseDateToMillis(dateStr: String): Long {
        return try {
            dateFormat.parse(dateStr)?.time ?: System.currentTimeMillis()
        } catch (_: Exception) {
            System.currentTimeMillis()
        }
    }

    val defaultSubjects = listOf(
        // ==================== FOUNDATION COURSES ====================
        Subject(
            id = "math1",
            name = "Mathematics for Data Science 1",
            code = "MA1001",
            level = CourseLevel.FOUNDATION,
            colorHex = 0xFF2563EB,
            isEnrolled = true,
            formulaType = SubjectFormulaType.FOUNDATION_STANDARD,
            formulaDescription = "T = max(0.6F + 0.3max(Qz1, Qz2), 0.45F + 0.25Qz1 + 0.3Qz2)",
            eligibilityCriteria = "Best 5 out of first 7 weeks GA scores >= 40/100 AND attendance in one of the 2 quizzes",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "english1",
            name = "English 1",
            code = "HS1001",
            level = CourseLevel.FOUNDATION,
            colorHex = 0xFF0D9488,
            isEnrolled = false,
            formulaType = SubjectFormulaType.FOUNDATION_STANDARD,
            formulaDescription = "T = max(0.6F + 0.3max(Qz1, Qz2), 0.45F + 0.25Qz1 + 0.3Qz2)",
            eligibilityCriteria = "Best 5 out of first 7 weeks GA scores >= 40/100 AND attendance in one quiz",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "ct",
            name = "Computational Thinking",
            code = "CS1001",
            level = CourseLevel.FOUNDATION,
            colorHex = 0xFF7C3AED,
            isEnrolled = false,
            formulaType = SubjectFormulaType.FOUNDATION_STANDARD,
            formulaDescription = "T = max(0.6F + 0.3max(Qz1, Qz2), 0.45F + 0.25Qz1 + 0.3Qz2)",
            eligibilityCriteria = "Best 5 of first 7 weeks >= 40/100 AND attendance in one quiz",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "stats1",
            name = "Statistics for Data Science 1",
            code = "MA1002",
            level = CourseLevel.FOUNDATION,
            colorHex = 0xFF0284C7,
            isEnrolled = true,
            formulaType = SubjectFormulaType.FOUNDATION_STATS1,
            formulaDescription = "T = max(0.6F + 0.3max(Qz1, Qz2), 0.45F + 0.25Qz1 + 0.3Qz2) + Bonus (capped to 5)",
            eligibilityCriteria = "Best 5 of first 7 weeks >= 40/100 AND attendance in one quiz. Extra activities added only on pass.",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "math2",
            name = "Mathematics for Data Science 2",
            code = "MA1003",
            level = CourseLevel.FOUNDATION,
            colorHex = 0xFF3B82F6,
            isEnrolled = false,
            formulaType = SubjectFormulaType.FOUNDATION_MATH2,
            formulaDescription = "T = max(0.6F + 0.3max(Qz1, Qz2), 0.45F + 0.25Qz1 + 0.3Qz2) + Bonus (capped to 6, max 100)",
            eligibilityCriteria = "Best 5 of first 7 weeks >= 40/100 AND attendance in one quiz",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "english2",
            name = "English 2",
            code = "HS1002",
            level = CourseLevel.FOUNDATION,
            colorHex = 0xFF059669,
            isEnrolled = false,
            formulaType = SubjectFormulaType.FOUNDATION_STANDARD,
            formulaDescription = "T = max(0.6F + 0.3max(Qz1, Qz2), 0.45F + 0.25Qz1 + 0.3Qz2)",
            eligibilityCriteria = "Best 5 of first 7 weeks GA scores >= 40/100 AND attendance in one quiz",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "python",
            name = "Programming in Python",
            code = "CS1002",
            level = CourseLevel.FOUNDATION,
            colorHex = 0xFFD97706,
            isEnrolled = true,
            formulaType = SubjectFormulaType.FOUNDATION_PYTHON,
            formulaDescription = "T = 0.15Qz1 + 0.4F + 0.25max(PE1, PE2) + 0.2min(PE1, PE2)",
            eligibilityCriteria = "SCT completed + A1-A4 >= 40 (for OPPE1) + A5-A8 >= 40 & best 5/7 >= 40 (for OPPE2). Score >= 40 in one OPPE required.",
            hasOppe = true,
            hasQuiz1 = true,
            hasQuiz2 = false,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "stats2",
            name = "Statistics for Data Science 2",
            code = "MA1004",
            level = CourseLevel.FOUNDATION,
            colorHex = 0xFF6366F1,
            isEnrolled = false,
            formulaType = SubjectFormulaType.FOUNDATION_STATS1,
            formulaDescription = "T = max(0.6F + 0.3max(Qz1, Qz2), 0.45F + 0.25Qz1 + 0.3Qz2) + Bonus (capped to 5)",
            eligibilityCriteria = "Best 5 of first 7 weeks >= 40/100 AND attendance in one quiz. Peer reviews required for Extra activity.",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),

        // ==================== DIPLOMA COURSES ====================
        Subject(
            id = "mlf",
            name = "Machine Learning Foundations",
            code = "CS2004",
            level = CourseLevel.DIPLOMA,
            colorHex = 0xFF4338CA,
            isEnrolled = true,
            formulaType = SubjectFormulaType.DIPLOMA_MLF,
            formulaDescription = "T = max (0.6F + 0.3max(Qz1, Qz2), 0.45F + 0.25Qz1 + 0.3Qz2) + BONUS (capped to 5)",
            eligibilityCriteria = "Best 5 of first 7 weeks >= 40 AND attend 1 quiz. Bonus marks from hard questions in quizzes & end term.",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "mlt",
            name = "Machine Learning Techniques",
            code = "CS2007",
            level = CourseLevel.DIPLOMA,
            colorHex = 0xFF6D28D9,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DIPLOMA_MLT,
            formulaDescription = "T = 0.05GAA + max(0.6F + 0.25max(Qz1, Qz2), 0.4F + 0.25Qz1 + 0.3Qz2) + Bonus(3)",
            eligibilityCriteria = "Best 5 of 7 >= 40 AND attend 1 quiz. Bonus 3 if avg of all assignments >= 40.",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "mlp",
            name = "Machine Learning Practice",
            code = "CS2008",
            level = CourseLevel.DIPLOMA,
            colorHex = 0xFFBE185D,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DIPLOMA_MLP,
            formulaDescription = "T = 0.05 GAA + 0.40 F + 0.20 OPPE1 + 0.20 OPPE2 + 0.15 KA",
            eligibilityCriteria = "Best 5 of 7 >= 40, OPPE SCT mandatory, 1 OPPE score >= 40. 3 Kaggle Assignments.",
            hasOppe = true,
            hasQuiz1 = false,
            hasQuiz2 = false,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "bdm",
            name = "Business Data Management",
            code = "MS2001",
            level = CourseLevel.DIPLOMA,
            colorHex = 0xFF047857,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DIPLOMA_BDM,
            formulaDescription = "T = 0.05GAA + max(0.6F + 0.25max(Qz1, Qz2), 0.4F + 0.25Qz1 + 0.3Qz2)",
            eligibilityCriteria = "Best 5 of 7 weekly GA >= 40 AND attend at least 1 quiz.",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "ba",
            name = "Business Analytics",
            code = "MS2002",
            level = CourseLevel.DIPLOMA,
            colorHex = 0xFFB45309,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DIPLOMA_BA,
            formulaDescription = "T = Qz + A + F, where Qz = 2*(0.7*max(Q1,Q2) + 0.3*min(Q1,Q2)), A = Best 2 of 3 (each out of 10)",
            eligibilityCriteria = "Submit >= 1 of 3 assignments AND attend 1 quiz. Final Exam score F >= 10/40 mandatory.",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "tds",
            name = "Tools in Data Science",
            code = "SE2002",
            level = CourseLevel.DIPLOMA,
            colorHex = 0xFF0E7490,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DIPLOMA_TDS,
            formulaDescription = "T = 0.2GAA + 0.2ROE + 0.2P1 + 0.2P2 + 0.2F",
            eligibilityCriteria = "Best 2 of first 4 GAs >= 40. Remote Online Exam (Nov 29) + 2 Projects (Nov 9 & Dec 13) + Final Exam.",
            hasOppe = false,
            hasQuiz1 = false,
            hasQuiz2 = false,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "pdsa",
            name = "Programming, Data Structures & Algorithms",
            code = "CS2002",
            level = CourseLevel.DIPLOMA,
            colorHex = 0xFF4F46E5,
            isEnrolled = true,
            formulaType = SubjectFormulaType.DIPLOMA_PDSA,
            formulaDescription = "T = 0.05GAA + 0.2OP + 0.45F + max(0.2max(Qz1, Qz2), 0.1Qz1 + 0.2Qz2)",
            eligibilityCriteria = "SCT completed + A2-A8 >= 40 for OPPE. Best 5 of 7 >= 40 for Final Exam.",
            hasOppe = true,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "dbms",
            name = "Database Management Systems",
            code = "CS2001",
            level = CourseLevel.DIPLOMA,
            colorHex = 0xFF1D4ED8,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DIPLOMA_DBMS,
            formulaDescription = "T = 0.03GAA2 + 0.02GAA3 + 0.2OPPE + 0.45F + max(0.2max(Qz1, Qz2), 0.1Qz1 + 0.2Qz2)",
            eligibilityCriteria = "Best 5 of 7 >= 40 + attend 1 quiz. OPPE (Dec 20 / Jan 3) requires >= 35% AND Python-DB question correct.",
            hasOppe = true,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "mad1",
            name = "Application Development 1",
            code = "CS2003",
            level = CourseLevel.DIPLOMA,
            colorHex = 0xFF15803D,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DIPLOMA_APP_DEV1,
            formulaDescription = "T = 0.05GLA + max(0.6F + 0.25max(Qz1, Qz2), 0.4F + 0.25Qz1 + 0.3Qz2)",
            eligibilityCriteria = "GLA = 70% best 2 of first 5 [2..6] + 30% of W7. Best 5 of 7 >= 40 + attend 1 quiz.",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "java",
            name = "Programming Concepts using Java",
            code = "CS2005",
            level = CourseLevel.DIPLOMA,
            colorHex = 0xFFC026D3,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DIPLOMA_JAVA,
            formulaDescription = "T = 0.05GAA + 0.2max(PE1, PE2) + 0.45F + max(0.2max(Qz1, Qz2), 0.1Qz1 + 0.2Qz2) + 0.1min(PE1, PE2)",
            eligibilityCriteria = "A2-A4 >= 40 for OPPE1 (Nov 22); A5-A8 >= 40 for OPPE2 (Dec 20 & Jan 3). Attending ET + OPPE >= 30% required.",
            hasOppe = true,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "sc",
            name = "System Commands",
            code = "SE2001",
            level = CourseLevel.DIPLOMA,
            colorHex = 0xFF475569,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DIPLOMA_SYS_COMMANDS,
            formulaDescription = "T = 0.05GAA + 0.25Qz1 + 0.3OPPE + 0.3F + 0.1BPTA",
            eligibilityCriteria = "Avg first 3 BPT >= 40 for OPPE. OPPE score >= 40 required for course grade. 4 BPTs.",
            hasOppe = true,
            hasQuiz1 = true,
            hasQuiz2 = false,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "mad2",
            name = "Application Development 2",
            code = "CS2006",
            level = CourseLevel.DIPLOMA,
            colorHex = 0xFF059669,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DIPLOMA_APP_DEV2,
            formulaDescription = "T = 0.05GAA + max(0.6F + 0.25max(Qz1, Qz2), 0.4F + 0.25Qz1 + 0.3Qz2)",
            eligibilityCriteria = "GAA = Avg of weeks 1 & 2 programming assignments. Best 5 of 7 >= 40 + attend 1 quiz.",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "dl_genai",
            name = "Intro to Deep Learning & Generative AI",
            code = "DA2001",
            level = CourseLevel.DIPLOMA,
            colorHex = 0xFF9333EA,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DIPLOMA_DL_GENAI,
            formulaDescription = "T = 0.1GAA + 0.2Qz1 + 0.2Qz2 + 0.25F + 0.1NPPE1 + 0.15NPPE2",
            eligibilityCriteria = "Best 5 of first 5 weekly assessments >= 40 + attend 1 quiz. 2 Non-Proctored Programming Assignments.",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),

        // ==================== DEGREE LEVEL COURSES ====================
        Subject(
            id = "soft_testing",
            name = "Software Testing",
            code = "CS3002",
            level = CourseLevel.DEGREE,
            colorHex = 0xFFE11D48,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DEGREE_SOFTWARE_TESTING,
            formulaDescription = "T = 0.1GAA + 0.4F + 0.25Qz1 + 0.25Qz2",
            eligibilityCriteria = "Best 5 of first 7 >= 40 + attend 1 quiz. GAA = Avg of first 10 weekly GAs.",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "soft_eng",
            name = "Software Engineering",
            code = "CS3001",
            level = CourseLevel.DEGREE,
            colorHex = 0xFFD97706,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DEGREE_SOFTWARE_ENG,
            formulaDescription = "T = 0.05GAA + 0.2Qz2 + 0.4F + 0.1GP1 + 0.1GP2 + 0.1PP + 0.05CP",
            eligibilityCriteria = "Best 5 of 7 >= 40 + submission of Group Project Milestones 1-2. No Quiz 1.",
            hasOppe = false,
            hasQuiz1 = false,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "deep_learning",
            name = "Deep Learning",
            code = "CS3004",
            level = CourseLevel.DEGREE,
            colorHex = 0xFF7C3AED,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DEGREE_DEEP_LEARNING,
            formulaDescription = "T = 0.05GAA + 0.25Qz1 + 0.25Qz2 + 0.45F + Bonus(capped 5)",
            eligibilityCriteria = "Best 5 of 7 >= 40 + attend 1 quiz. 3 programming assignments for bonus.",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "ai_search",
            name = "AI: Search Methods for Problem Solving",
            code = "CS3003",
            level = CourseLevel.DEGREE,
            colorHex = 0xFF2563EB,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DEGREE_AI_SEARCH,
            formulaDescription = "T = 0.1GAA + 0.4F + 0.25Qz1 + 0.25Qz2 + Bonus(5)",
            eligibilityCriteria = "Best 5 of 7 >= 40 + attend 1 quiz. Bonus 5 marks on programming assignment if passed.",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "spg",
            name = "Strategies for Professional Growth",
            code = "GN3001",
            level = CourseLevel.DEGREE,
            colorHex = 0xFF059669,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DEGREE_SPG,
            formulaDescription = "T = 0.10*GAA + 0.25*GP + 0.30*Qz2 + 0.35*F",
            eligibilityCriteria = "Best 5 of first 7 weeks GA >= 40. Group Project Milestones I & II.",
            hasOppe = false,
            hasQuiz1 = false,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "prog_c",
            name = "Programming in C",
            code = "CS3005",
            level = CourseLevel.DEGREE,
            colorHex = 0xFF0284C7,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DEGREE_PROG_C,
            formulaDescription = "T = 0.10GAA + 0.20Qz1 + 0.20OPPE1 + 0.20OPPE2 + 0.30F",
            eligibilityCriteria = "Best 5 of 7 >= 40 for ET. Score in 1 OPPE >= 40 required. No Quiz 2.",
            hasOppe = true,
            hasQuiz1 = true,
            hasQuiz2 = false,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "big_data",
            name = "Introduction to Big Data",
            code = "DA5001",
            level = CourseLevel.DEGREE,
            colorHex = 0xFFEA580C,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DEGREE_BIG_DATA,
            formulaDescription = "T = 0.1 GAA + 0.3 F + 0.2 OPPE1 + 0.4 OPPE2 + Bonus (5 marks)",
            eligibilityCriteria = "Best 5 of 7 >= 40. No Quiz 1 & 2. OPPE 1 (Nov 22), OPPE 2 (Dec 20). Score in 1 OPPE >= 40 required.",
            hasOppe = true,
            hasQuiz1 = false,
            hasQuiz2 = false,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "llm",
            name = "Large Language Models",
            code = "DA5004",
            level = CourseLevel.DEGREE,
            colorHex = 0xFF6366F1,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DEGREE_LLM,
            formulaDescription = "T = 0.05GAA + 0.35F + 0.3Qz1 + 0.3Qz2 + Bonus(5)",
            eligibilityCriteria = "Best 5 of 7 >= 40 + attend 1 quiz. GAA = Avg of 7 assignments till week 9.",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "mlops",
            name = "MLOps (Machine Learning Operations)",
            code = "DA5014",
            level = CourseLevel.DEGREE,
            colorHex = 0xFF0891B2,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DEGREE_MLOPS,
            formulaDescription = "T = 0.2GAA + 0.3F + 0.25OPPE1 + 0.25OPPE2 + Bonus (capped 100)",
            eligibilityCriteria = "Best 5 of 7 >= 40. No Quiz 1 & 2. 2 OPPEs (Nov 22 & Jan 3). Requires GCP setup.",
            hasOppe = true,
            hasQuiz1 = false,
            hasQuiz2 = false,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "math_thinking",
            name = "Mathematical Thinking",
            code = "MA2001",
            level = CourseLevel.DEGREE,
            colorHex = 0xFF10B981,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DEGREE_MATH_THINKING,
            formulaDescription = "T = 0.1GAA + max(0.6F + 0.2max(Qz1, Qz2), 0.4F + 0.2Qz1 + 0.3Qz2)",
            eligibilityCriteria = "Best 5 of first 7 weeks >= 40/100 AND attendance in one quiz",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "digital_ic",
            name = "Digital IC Design",
            code = "EE5102",
            level = CourseLevel.DEGREE,
            colorHex = 0xFF8B5CF6,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DEGREE_DIGITAL_IC,
            formulaDescription = "T = 0.1GAA + 0.3P + max(0.3F + 0.2max(Qz1,Qz2), 0.3F + 0.15Qz1 + 0.15Qz2)",
            eligibilityCriteria = "Best 5 of first 7 weeks >= 40 + attend 1 quiz. Includes Course Project (P).",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "financial_forensics",
            name = "Financial Forensics",
            code = "MS4003",
            level = CourseLevel.DEGREE,
            colorHex = 0xFF0D9488,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DEGREE_FINANCIAL_FORENSICS,
            formulaDescription = "T = 0.1GAA + 0.25Qz1 + 0.30GP1 + 0.35F",
            eligibilityCriteria = "Best 5 out of first 7 weeks >= 40/100. Remote Group Project 1 (GP1).",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "dl_cv",
            name = "Deep Learning for CV",
            code = "DA5006",
            level = CourseLevel.DEGREE,
            colorHex = 0xFF7C3AED,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DEGREE_DL_CV,
            formulaDescription = "T = 0.1GAA + 0.4F + 0.25Qz1 + 0.25Qz2 + Bonus (5 marks)",
            eligibilityCriteria = "Best 5 of 7 >= 40 AND attendance in 1 quiz. Optional project bonus.",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "dl_practice",
            name = "Deep Learning Practice",
            code = "DA5013",
            level = CourseLevel.DEGREE,
            colorHex = 0xFF9333EA,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DEGREE_DL_PRACTICE,
            formulaDescription = "T = 0.05GA + 0.15Q1 + 0.15Q2 + 0.15Q3 + 0.25*(NPPE_avg) + 0.25*Viva",
            eligibilityCriteria = "Viva score >= 40%. 3 NPPEs (Nov 9, Nov 30, Jan 4).",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "ds_ai_lab",
            name = "Data Science and AI Lab",
            code = "DA4001",
            level = CourseLevel.DEGREE,
            colorHex = 0xFF2563EB,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DEGREE_DSAI_LAB,
            formulaDescription = "T = 0.05GAA + 0.25Qz2 + 0.4P + 0.3V + Bonus (5 marks)",
            eligibilityCriteria = "Attending Project Presentation AND Viva >= 55%. No Quiz 1, Quiz 2 on Dec 5.",
            hasOppe = false,
            hasQuiz1 = false,
            hasQuiz2 = true,
            hasEndTerm = false,
            credits = 4
        ),
        Subject(
            id = "app_dev_lab",
            name = "Application Development Lab",
            code = "CS4010",
            level = CourseLevel.DEGREE,
            colorHex = 0xFF15803D,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DEGREE_APPDEV_LAB,
            formulaDescription = "T = 0.20*Qz2 + 0.30*GAA + 0.50*Viva",
            eligibilityCriteria = "First 8 weeks GA >= 40, attempt W4/W5 lab assignments, Quiz 2. Score >= 25/50 in Viva.",
            hasOppe = false,
            hasQuiz1 = false,
            hasQuiz2 = true,
            hasEndTerm = false,
            credits = 4
        ),
        Subject(
            id = "bioinformatics",
            name = "Big Data and Biological Networks",
            code = "BT4002",
            level = CourseLevel.DEGREE,
            colorHex = 0xFF059669,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DEGREE_BIOINFORMATICS,
            formulaDescription = "T = 0.1GAA + 0.4F + 0.25Qz1 + 0.25Qz2",
            eligibilityCriteria = "Best 5 of 7 >= 40 + attend 1 quiz. GAA = best 8 of first 9.",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "industry_4",
            name = "Industry 4.0",
            code = "MS4001",
            level = CourseLevel.DEGREE,
            colorHex = 0xFFB45309,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DEGREE_INDUSTRY_4,
            formulaDescription = "T = 15(Quizzes) + 5(Yield Game) + 40(Best 2 Assignments) + 30(EndTerm) + 10(Project)",
            eligibilityCriteria = "Submit >= 1 of first 3 assignments + attend 1 quiz + (game or project).",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "math_genai",
            name = "Mathematical Foundations of Generative AI",
            code = "DA5002",
            level = CourseLevel.DEGREE,
            colorHex = 0xFF4338CA,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DEGREE_MATH_GENAI,
            formulaDescription = "T = 0.05GAA + 0.4F + 0.2Qz1 + 0.2Qz2 + 0.15NPPE",
            eligibilityCriteria = "Best 5 of 7 >= 40. NPPE Dec 11-14.",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "speech_tech",
            name = "Speech Technology",
            code = "EE4001",
            level = CourseLevel.DEGREE,
            colorHex = 0xFF0284C7,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DEGREE_SPEECH_TECH,
            formulaDescription = "T = 0.15GAA + 0.15V + 0.3F + 0.20Qz1 + 0.20Qz2",
            eligibilityCriteria = "Best 5 of 7 >= 40 + attend 1 quiz.",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "design_thinking",
            name = "Design Thinking for Data-Driven App Dev",
            code = "MS4002",
            level = CourseLevel.DEGREE,
            colorHex = 0xFFC026D3,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DEGREE_DESIGN_THINKING,
            formulaDescription = "T = 0.1GAA + 0.1GP1 + 0.1GP2 + 0.2GP3 + 0.2Qz2 + 0.3F",
            eligibilityCriteria = "Best 5 of 7 >= 40. Submit final project > 60/100.",
            hasOppe = false,
            hasQuiz1 = false,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "linear_models",
            name = "Linear Models with Applications",
            code = "MA3015",
            level = CourseLevel.DEGREE,
            colorHex = 0xFF3B82F6,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DEGREE_LINEAR_MODELS,
            formulaDescription = "T = 0.1GAA + 0.25P + max(0.35F + 0.2max(Q1,Q2), 0.35F + 0.15Q1 + 0.15Q2)",
            eligibilityCriteria = "Best 5 of first 7 >= 40 + attend 1 quiz.",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "rl",
            name = "Special Topics in ML (Reinforcement Learning)",
            code = "DA5007",
            level = CourseLevel.DEGREE,
            colorHex = 0xFF6366F1,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DEGREE_REINFORCEMENT_LEARNING,
            formulaDescription = "T = 0.05GAA + 0.25GPA + 0.2Qz1 + 0.2Qz2 + 0.3F + BONUS",
            eligibilityCriteria = "Best 5 of 7 >= 40 + attend 1 quiz. Bonus 5 marks for programming assignments.",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "corp_fin",
            name = "Corporate Finance",
            code = "MS3034",
            level = CourseLevel.DEGREE,
            colorHex = 0xFF0D9488,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DEGREE_CORPORATE_FINANCE,
            formulaDescription = "T = 0.1GAA + 0.4F + 0.2Qz1 + 0.3Qz2",
            eligibilityCriteria = "Best 5 of 7 >= 40 + attend 1 quiz.",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "comp_net",
            name = "Computer Networks",
            code = "CS4024",
            level = CourseLevel.DEGREE,
            colorHex = 0xFF1D4ED8,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DEGREE_COMPUTER_NETWORKS,
            formulaDescription = "T = 0.1GAA + 0.30F + 0.25Qz1 + 0.25Qz2 + 0.1 Programming Assignment",
            eligibilityCriteria = "Best 5 of 7 >= 40 + attend 1 quiz.",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "comp_sys_design",
            name = "Computer Systems Design",
            code = "CS3031",
            level = CourseLevel.DEGREE,
            colorHex = 0xFF475569,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DEGREE_COMPUTER_SYSTEMS,
            formulaDescription = "T = 0.1GAA + 0.4F + 0.2Qz1 + 0.25Qz2 + 0.05 CircuitVerse Assignment",
            eligibilityCriteria = "Best 5 of 7 >= 40 + attend 1 quiz.",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "game_theory",
            name = "Game Theory and Strategy",
            code = "MS4023",
            level = CourseLevel.DEGREE,
            colorHex = 0xFFD97706,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DEGREE_GAME_THEORY,
            formulaDescription = "T = 0.1GAA + 0.4F + 0.25Qz1 + 0.25Qz2",
            eligibilityCriteria = "Best 5 of 7 >= 40 + attend 1 quiz.",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "ads",
            name = "Algorithms for Data Science (ADS)",
            code = "DA5003",
            level = CourseLevel.DEGREE,
            colorHex = 0xFF4F46E5,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DEGREE_ADS,
            formulaDescription = "T = 0.1GAA + 0.4F + 0.25Q1 + 0.25Qz2 + BONUS (PA)",
            eligibilityCriteria = "Best 2 of first 3 assignment scores (W2, W4, W6) >= 40/100.",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "compiler_design",
            name = "Compiler Design",
            code = "CS4032",
            level = CourseLevel.DEGREE,
            colorHex = 0xFF6D28D9,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DEGREE_STANDARD,
            formulaDescription = "T = 0.1GAA + 0.4F + 0.25Qz1 + 0.25Qz2",
            eligibilityCriteria = "Best 5 of 7 >= 40 + attend 1 quiz.",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "sequential_decisions",
            name = "Sequential Decision Making",
            code = "DA6004",
            level = CourseLevel.DEGREE,
            colorHex = 0xFF7C3AED,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DEGREE_ADS,
            formulaDescription = "T = 0.1GAA + 0.4F + 0.25Q1 + 0.25Qz2 + BONUS (PA)",
            eligibilityCriteria = "Best 5 of 7 >= 40 + attend 1 quiz.",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "os",
            name = "Operating Systems",
            code = "CS4022",
            level = CourseLevel.DEGREE,
            colorHex = 0xFF0284C7,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DEGREE_STANDARD,
            formulaDescription = "T = 0.1GAA + 0.4F + 0.25Qz1 + 0.25Qz2",
            eligibilityCriteria = "Best 5 of 7 >= 40 + attend 1 quiz.",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "discrete_math",
            name = "Discrete Mathematics",
            code = "MA3001",
            level = CourseLevel.DEGREE,
            colorHex = 0xFF2563EB,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DEGREE_STANDARD,
            formulaDescription = "T = 0.1GAA + 0.4F + 0.25Qz1 + 0.25Qz2",
            eligibilityCriteria = "Best 5 of 7 >= 40 + attend 1 quiz.",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "e_testing",
            name = "Electronic Testing and Measurement",
            code = "EE2105",
            level = CourseLevel.DEGREE,
            colorHex = 0xFF0891B2,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DEGREE_E_TESTING,
            formulaDescription = "T = 0.1GAA + max(0.6F + 0.2max(Q1,Q2), 0.5F + 0.2Q1 + 0.2Q2)",
            eligibilityCriteria = "Best 5 of first 6 weeks >= 40 + attend 1 quiz.",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "comm_sys",
            name = "Communication Systems",
            code = "EE4103",
            level = CourseLevel.DEGREE,
            colorHex = 0xFF059669,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DEGREE_COMM_SYSTEMS,
            formulaDescription = "T = 0.1GAA + max(0.6F + 0.2max(Q1,Q2), 0.4F + 0.2Q1 + 0.3Q2)",
            eligibilityCriteria = "Best 5 of 7 >= 40 + attend 1 quiz. Uploading worked out GA solution mandatory.",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "digital_sys_design",
            name = "Digital System Design",
            code = "EE2103",
            level = CourseLevel.DEGREE,
            colorHex = 0xFFD97706,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DEGREE_DIGITAL_SYS_DESIGN,
            formulaDescription = "T = 0.05GAA + max(0.6F + 0.25max(Q1,Q2), 0.5F + 0.2Q1 + 0.25Q2)",
            eligibilityCriteria = "Best 5 of 7 >= 40 + attend 1 quiz. 5% direct questions from GAs.",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        ),
        Subject(
            id = "comp_org",
            name = "Computer Organization",
            code = "EE2106",
            level = CourseLevel.DEGREE,
            colorHex = 0xFF7C3AED,
            isEnrolled = false,
            formulaType = SubjectFormulaType.DEGREE_COMP_ORG,
            formulaDescription = "T = 0.05GAA + max(0.6F + 0.25max(Q1,Q2), 0.5F + 0.2Q1 + 0.25Q2)",
            eligibilityCriteria = "Best 5 of 7 >= 40 + attend 1 quiz. 5% direct questions from GAs.",
            hasOppe = false,
            hasQuiz1 = true,
            hasQuiz2 = true,
            hasEndTerm = true,
            credits = 4
        )
    )

    // Generate accurate assignments for a given subject based on Sep 2026 Term schedule
    fun generateDefaultAssignmentsForSubject(subject: Subject): List<Assignment> {
        val list = mutableListOf<Assignment>()

        // Official Sep 2026 Term Deadlines
        val termDeadlines = listOf(
            "2026-10-11", // W1 (Sunday)
            "2026-10-18", // W2 (Sunday)
            "2026-10-25", // W3 (Sunday)
            "2026-11-01", // W4 (Sunday - OPPE 1 eligibility closes)
            "2026-11-11", // W5 (Wednesday)
            "2026-11-18", // W6 (Wednesday)
            "2026-11-25", // W7 (Wednesday - End term eligibility closes)
            "2026-11-29", // W8 (Sunday - OPPE 2 eligibility closes)
            "2026-12-06", // W9 (Sunday)
            "2026-12-13", // W10 (Sunday - GAA calculation closes)
            "2026-12-23", // W11 (Wednesday)
            "2026-12-23"  // W12 (Wednesday)
        )

        // Generate weekly graded assignments
        for (i in 1..12) {
            val date = termDeadlines[i - 1]
            val isEligibilityWeek = (i <= 7)
            val priority = when {
                i == 7 -> Priority.CRITICAL // End term eligibility cutoff
                i == 4 -> Priority.HIGH     // OPPE 1 eligibility cutoff
                i == 8 -> Priority.HIGH     // OPPE 2 eligibility cutoff
                i in 1..5 -> Priority.HIGH
                else -> Priority.MEDIUM
            }

            list.add(
                Assignment(
                    subjectId = subject.id,
                    subjectName = subject.name,
                    title = "Week $i Graded Assignment",
                    weekNumber = i,
                    type = AssignmentType.WEEKLY_GA,
                    dueDate = date,
                    dueTimestamp = parseDateToMillis(date),
                    priority = priority,
                    status = AssignmentStatus.PENDING,
                    maxScore = 100f,
                    score = null,
                    weight = if (subject.level == CourseLevel.FOUNDATION) 0f else 5f,
                    notes = when (i) {
                        4 -> "OPPE 1 eligibility cutoff closes after Week 4"
                        7 -> "CRITICAL: End Term eligibility cutoff closes (Best 5 of 7 >= 40 required)"
                        8 -> "OPPE 2 eligibility cutoff closes after Week 8"
                        10 -> "GAA calculation closes after Week 10"
                        else -> "Mandatory preparation for Quiz and End Term exams"
                    },
                    reminderEnabled = true,
                    reminderHoursBefore = 24
                )
            )

            // If programming course, add GrPA
            if (subject.id in listOf("python", "pdsa", "java", "prog_c") && i in 1..8) {
                list.add(
                    Assignment(
                        subjectId = subject.id,
                        subjectName = subject.name,
                        title = "Week $i GrPA (Programming Practice)",
                        weekNumber = i,
                        type = AssignmentType.PROGRAMMING,
                        dueDate = date,
                        dueTimestamp = parseDateToMillis(date),
                        priority = if (i <= 4) Priority.CRITICAL else Priority.HIGH,
                        status = AssignmentStatus.PENDING,
                        maxScore = 100f,
                        score = null,
                        weight = 10f,
                        notes = "Required average >= 40 for OPPE eligibility",
                        reminderEnabled = true,
                        reminderHoursBefore = 48
                    )
                )
            }
        }

        // Add Mock Quizzes / Mock Assignments
        list.add(
            Assignment(
                subjectId = subject.id,
                subjectName = subject.name,
                title = "Mock Quiz 1 (Mock Activity)",
                weekNumber = 4,
                type = AssignmentType.EXTRA_ACTIVITY,
                dueDate = "2026-11-08",
                dueTimestamp = parseDateToMillis("2026-11-08"),
                priority = Priority.HIGH,
                status = AssignmentStatus.PENDING,
                maxScore = 100f,
                score = null,
                weight = 2f,
                notes = "Bonus marks: If avg of mock tests before Quiz 1 & 2 >= 40, student gets 2 bonus marks.",
                reminderEnabled = true
            )
        )

        // Quizzes (In-person at centers)
        if (subject.hasQuiz1) {
            list.add(
                Assignment(
                    subjectId = subject.id,
                    subjectName = subject.name,
                    title = "Quiz 1 (In-Person Exam)",
                    type = AssignmentType.QUIZ,
                    dueDate = "2026-11-15",
                    dueTimestamp = parseDateToMillis("2026-11-15"),
                    priority = Priority.CRITICAL,
                    status = AssignmentStatus.PENDING,
                    maxScore = 100f,
                    score = null,
                    weight = 25f,
                    notes = "In-person at designated centres (2:00 PM - 6:00 PM)",
                    reminderEnabled = true,
                    reminderHoursBefore = 48
                )
            )
        }

        if (subject.hasQuiz2) {
            list.add(
                Assignment(
                    subjectId = subject.id,
                    subjectName = subject.name,
                    title = "Quiz 2 (In-Person Exam)",
                    type = AssignmentType.QUIZ,
                    dueDate = "2026-12-05",
                    dueTimestamp = parseDateToMillis("2026-12-05"),
                    priority = Priority.CRITICAL,
                    status = AssignmentStatus.PENDING,
                    maxScore = 100f,
                    score = null,
                    weight = 25f,
                    notes = "In-person at designated centres (2:00 PM - 6:00 PM on Saturday)",
                    reminderEnabled = true,
                    reminderHoursBefore = 48
                )
            )
        }

        // OPPE Exams for programming courses
        if (subject.hasOppe) {
            list.add(
                Assignment(
                    subjectId = subject.id,
                    subjectName = subject.name,
                    title = "OPPE 1 (Online Proctored Exam)",
                    type = AssignmentType.OPPE,
                    dueDate = "2026-11-22",
                    dueTimestamp = parseDateToMillis("2026-11-22"),
                    priority = Priority.CRITICAL,
                    status = AssignmentStatus.PENDING,
                    maxScore = 100f,
                    score = null,
                    weight = 20f,
                    notes = "Remote proctored online exam. Requires OPPE SCT completion.",
                    reminderEnabled = true,
                    reminderHoursBefore = 48
                )
            )
            list.add(
                Assignment(
                    subjectId = subject.id,
                    subjectName = subject.name,
                    title = "OPPE 2 (Online Proctored Exam)",
                    type = AssignmentType.OPPE,
                    dueDate = if (subject.id in listOf("python", "mlops")) "2027-01-03" else "2026-12-20",
                    dueTimestamp = parseDateToMillis(if (subject.id in listOf("python", "mlops")) "2027-01-03" else "2026-12-20"),
                    priority = Priority.CRITICAL,
                    status = AssignmentStatus.PENDING,
                    maxScore = 100f,
                    score = null,
                    weight = 20f,
                    notes = "Remote proctored online exam. Passing cutoff required for course grade.",
                    reminderEnabled = true,
                    reminderHoursBefore = 48
                )
            )
        }

        // End-Term Exam (Sunday, January 10, 2027)
        if (subject.hasEndTerm) {
            list.add(
                Assignment(
                    subjectId = subject.id,
                    subjectName = subject.name,
                    title = "End Term Exam (Final)",
                    type = AssignmentType.QUIZ,
                    dueDate = "2027-01-10",
                    dueTimestamp = parseDateToMillis("2027-01-10"),
                    priority = Priority.CRITICAL,
                    status = AssignmentStatus.PENDING,
                    maxScore = 100f,
                    score = null,
                    weight = 40f,
                    notes = "In-person at designated centres (9:00 AM - 12:00 PM, 2:00 PM - 5:00 PM)",
                    reminderEnabled = true,
                    reminderHoursBefore = 72
                )
            )
        }

        // Course-specific deliverables
        if (subject.id == "mlp") {
            list.add(
                Assignment(
                    subjectId = subject.id,
                    subjectName = subject.name,
                    title = "Kaggle Assignment 1 (KA 1)",
                    type = AssignmentType.PROJECT,
                    dueDate = "2026-11-11",
                    dueTimestamp = parseDateToMillis("2026-11-11"),
                    priority = Priority.HIGH,
                    status = AssignmentStatus.PENDING,
                    maxScore = 100f,
                    weight = 15f,
                    notes = "Release Nov 2, Peer Review deadline Nov 13",
                    reminderEnabled = true
                )
            )
            list.add(
                Assignment(
                    subjectId = subject.id,
                    subjectName = subject.name,
                    title = "Kaggle Assignment 2 (KA 2)",
                    type = AssignmentType.PROJECT,
                    dueDate = "2026-11-25",
                    dueTimestamp = parseDateToMillis("2026-11-25"),
                    priority = Priority.HIGH,
                    status = AssignmentStatus.PENDING,
                    maxScore = 100f,
                    weight = 15f,
                    notes = "Release Nov 17, Peer Review deadline Nov 27",
                    reminderEnabled = true
                )
            )
            list.add(
                Assignment(
                    subjectId = subject.id,
                    subjectName = subject.name,
                    title = "Kaggle Assignment 3 (KA 3)",
                    type = AssignmentType.PROJECT,
                    dueDate = "2026-12-14",
                    dueTimestamp = parseDateToMillis("2026-12-14"),
                    priority = Priority.HIGH,
                    status = AssignmentStatus.PENDING,
                    maxScore = 100f,
                    weight = 15f,
                    notes = "Release Dec 6, Peer Review deadline Dec 16",
                    reminderEnabled = true
                )
            )
        }

        if (subject.id == "sc") {
            val bptDates = listOf("2026-10-23", "2026-11-06", "2026-11-20", "2026-12-11")
            for (idx in 1..4) {
                list.add(
                    Assignment(
                        subjectId = subject.id,
                        subjectName = subject.name,
                        title = "BPT $idx (Biweekly Programming Test)",
                        type = AssignmentType.PROGRAMMING,
                        dueDate = bptDates[idx - 1],
                        dueTimestamp = parseDateToMillis(bptDates[idx - 1]),
                        priority = Priority.HIGH,
                        status = AssignmentStatus.PENDING,
                        maxScore = 100f,
                        weight = 10f,
                        notes = "Conducted in course VM. First 3 BPTs average >= 40 required for OPPE eligibility.",
                        reminderEnabled = true
                    )
                )
            }
        }

        if (subject.id == "tds") {
            list.add(
                Assignment(
                    subjectId = subject.id,
                    subjectName = subject.name,
                    title = "Take Home Project 1 (P1)",
                    type = AssignmentType.PROJECT,
                    dueDate = "2026-11-09",
                    dueTimestamp = parseDateToMillis("2026-11-09"),
                    priority = Priority.HIGH,
                    status = AssignmentStatus.PENDING,
                    maxScore = 100f,
                    weight = 20f,
                    notes = "Open internet project on TDS portal",
                    reminderEnabled = true
                )
            )
            list.add(
                Assignment(
                    subjectId = subject.id,
                    subjectName = subject.name,
                    title = "Remote Online Exam (ROE)",
                    type = AssignmentType.QUIZ,
                    dueDate = "2026-11-29",
                    dueTimestamp = parseDateToMillis("2026-11-29"),
                    priority = Priority.CRITICAL,
                    status = AssignmentStatus.PENDING,
                    maxScore = 100f,
                    weight = 20f,
                    notes = "45 mins remote online exam, open internet, non-proctored",
                    reminderEnabled = true
                )
            )
            list.add(
                Assignment(
                    subjectId = subject.id,
                    subjectName = subject.name,
                    title = "Take Home Project 2 (P2)",
                    type = AssignmentType.PROJECT,
                    dueDate = "2026-12-13",
                    dueTimestamp = parseDateToMillis("2026-12-13"),
                    priority = Priority.HIGH,
                    status = AssignmentStatus.PENDING,
                    maxScore = 100f,
                    weight = 20f,
                    notes = "Open internet project on TDS portal",
                    reminderEnabled = true
                )
            )
        }

        if (subject.id == "financial_forensics") {
            list.add(
                Assignment(
                    subjectId = subject.id,
                    subjectName = subject.name,
                    title = "Remote Group Project 1 (GP1)",
                    type = AssignmentType.PROJECT,
                    dueDate = "2026-11-20",
                    dueTimestamp = parseDateToMillis("2026-11-20"),
                    priority = Priority.HIGH,
                    status = AssignmentStatus.PENDING,
                    maxScore = 100f,
                    weight = 30f,
                    notes = "Team Project: Project submission and presentation (30% of total score)",
                    reminderEnabled = true
                )
            )
        }

        return list
    }
}
