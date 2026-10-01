package com.example.data.model

import kotlin.math.max
import kotlin.math.min

data class GradeInputs(
    val gaa: Float = 85f,
    val quiz1: Float = 75f,
    val quiz2: Float = 80f,
    val finalExam: Float = 70f,
    val oppe1: Float = 80f,
    val oppe2: Float = 85f,
    val projectOrLab: Float = 85f,
    val bonus: Float = 0f,
    val sctCompleted: Boolean = true,
    val attendedQuiz: Boolean = true,
    val attendedFinal: Boolean = true
)

data class GradeResult(
    val totalScore: Float,
    val letterGrade: String,
    val isEligibleForFinal: Boolean,
    val isEligibleForOppe: Boolean,
    val isPass: Boolean,
    val breakdownText: String,
    val eligibilityNote: String
)

object GradeCalculator {

    fun calculate(subject: Subject, inputs: GradeInputs): GradeResult {
        val q1 = inputs.quiz1.coerceIn(0f, 100f)
        val q2 = inputs.quiz2.coerceIn(0f, 100f)
        val f = inputs.finalExam.coerceIn(0f, 100f)
        val pe1 = inputs.oppe1.coerceIn(0f, 100f)
        val pe2 = inputs.oppe2.coerceIn(0f, 100f)
        val gaa = inputs.gaa.coerceIn(0f, 100f)
        val proj = inputs.projectOrLab.coerceIn(0f, 100f)
        val bonus = inputs.bonus.coerceIn(0f, 15f)

        var total = 0f
        var formulaExplanation = ""
        var isEligibleFinal = inputs.gaa >= 40f && inputs.attendedQuiz
        var isEligibleOppe = inputs.sctCompleted

        when (subject.formulaType) {
            SubjectFormulaType.FOUNDATION_STANDARD -> {
                // T = max (0.6F + 0.3max(Qz1, Qz2), 0.45F + 0.25Qz1 + 0.3Qz2)
                val opt1 = 0.60f * f + 0.30f * max(q1, q2)
                val opt2 = 0.45f * f + 0.25f * q1 + 0.30f * q2
                total = max(opt1, opt2)
                formulaExplanation = "Opt1 (0.6*F + 0.3*max(Q1,Q2)) = ${String.format("%.1f", opt1)}\n" +
                        "Opt2 (0.45*F + 0.25*Q1 + 0.3*Q2) = ${String.format("%.1f", opt2)}\n" +
                        "T = max(Opt1, Opt2)"
            }
            SubjectFormulaType.FOUNDATION_STATS1 -> {
                // T = max(...) + Bonus for Extra activity (capped to 5)
                val opt1 = 0.60f * f + 0.30f * max(q1, q2)
                val opt2 = 0.45f * f + 0.25f * q1 + 0.30f * q2
                val base = max(opt1, opt2)
                val cappedBonus = min(bonus, 5f)
                total = min(100f, base + if (base >= 40f) cappedBonus else 0f)
                formulaExplanation = "Base = max(Opt1: ${String.format("%.1f", opt1)}, Opt2: ${String.format("%.1f", opt2)}) = ${String.format("%.1f", base)}\n" +
                        "Extra Activity Bonus (capped 5): +${String.format("%.1f", cappedBonus)} (added only if passed)"
            }
            SubjectFormulaType.FOUNDATION_MATH2 -> {
                // T = max(...) + Bonus for Extra activity (capped to 6, max 100)
                val opt1 = 0.60f * f + 0.30f * max(q1, q2)
                val opt2 = 0.45f * f + 0.25f * q1 + 0.30f * q2
                val base = max(opt1, opt2)
                val cappedBonus = min(bonus, 6f)
                total = min(100f, base + cappedBonus)
                formulaExplanation = "Base = ${String.format("%.1f", base)} + Extra Activity Bonus: +${String.format("%.1f", cappedBonus)}"
            }
            SubjectFormulaType.FOUNDATION_PYTHON -> {
                // T = 0.15Qz1 + 0.4F + 0.25max(PE1, PE2) + 0.2min(PE1, PE2)
                val qzPart = 0.15f * q1
                val fPart = 0.40f * f
                val peMaxPart = 0.25f * max(pe1, pe2)
                val peMinPart = 0.20f * min(pe1, pe2)
                total = qzPart + fPart + peMaxPart + peMinPart
                val oppePassed = max(pe1, pe2) >= 40f
                isEligibleOppe = inputs.sctCompleted && gaa >= 40f
                isEligibleFinal = isEligibleOppe
                formulaExplanation = "0.15*Q1 (${String.format("%.1f", qzPart)}) + 0.4*F (${String.format("%.1f", fPart)}) + 0.25*max(PE) (${String.format("%.1f", peMaxPart)}) + 0.2*min(PE) (${String.format("%.1f", peMinPart)})\n" +
                        "Sep 2026: Must score >= 40 in at least one OPPE (Nov 22 or Jan 3) to pass!"
            }
            SubjectFormulaType.DIPLOMA_MLF -> {
                // Sep 2026: T = max (0.6F + 0.3max(Qz1, Qz2), 0.45F + 0.25Qz1 + 0.3Qz2) + BONUS (capped to 5)
                val opt1 = 0.60f * f + 0.30f * max(q1, q2)
                val opt2 = 0.45f * f + 0.25f * q1 + 0.30f * q2
                val base = max(opt1, opt2)
                val bonusPts = min(bonus, 5f)
                total = min(100f, base + bonusPts)
                formulaExplanation = "Base: max(Opt1: ${String.format("%.1f", opt1)}, Opt2: ${String.format("%.1f", opt2)}) = ${String.format("%.1f", base)} + Bonus from hard questions (+${String.format("%.1f", bonusPts)})"
            }
            SubjectFormulaType.DIPLOMA_MLT -> {
                // T = 0.05 GAA + max (0.6F + 0.25max(Qz1, Qz2), 0.4F + 0.25Qz1 + 0.3Qz2) + Bonus 3
                val gaaPart = 0.05f * gaa
                val opt1 = 0.60f * f + 0.25f * max(q1, q2)
                val opt2 = 0.40f * f + 0.25f * q1 + 0.30f * q2
                val base = gaaPart + max(opt1, opt2)
                val bonusPts = if (gaa >= 40f) min(bonus, 3f) else 0f
                total = min(100f, base + bonusPts)
                formulaExplanation = "0.05*GAA (${String.format("%.1f", gaaPart)}) + max(Opt1: ${String.format("%.1f", opt1)}, Opt2: ${String.format("%.1f", opt2)}) + Bonus (+${String.format("%.1f", bonusPts)})"
            }
            SubjectFormulaType.DIPLOMA_MLP -> {
                // Sep 2026: Total course score T = 0.05 GAA + 0.40 F + 0.20 OPPE1 + 0.20 OPPE2 + 0.15 KA
                val gaaPart = 0.05f * gaa
                val fPart = 0.40f * f
                val oppe1Part = 0.20f * pe1
                val oppe2Part = 0.20f * pe2
                val kaPart = 0.15f * proj
                total = gaaPart + fPart + oppe1Part + oppe2Part + kaPart
                formulaExplanation = "0.05*GAA (${String.format("%.1f", gaaPart)}) + 0.40*F (${String.format("%.1f", fPart)}) + 0.20*OPPE1 (${String.format("%.1f", oppe1Part)}) + 0.20*OPPE2 (${String.format("%.1f", oppe2Part)}) + 0.15*KA (${String.format("%.1f", kaPart)})"
            }
            SubjectFormulaType.DIPLOMA_BDM, SubjectFormulaType.DIPLOMA_APP_DEV2 -> {
                // T = 0.05 GAA + max(0.6F + 0.25max(Qz1, Qz2), 0.4F + 0.25Qz1 + 0.3Qz2)
                val gaaPart = 0.05f * gaa
                val opt1 = 0.60f * f + 0.25f * max(q1, q2)
                val opt2 = 0.40f * f + 0.25f * q1 + 0.30f * q2
                total = gaaPart + max(opt1, opt2)
                formulaExplanation = "0.05*GAA (${String.format("%.1f", gaaPart)}) + max(Opt1: ${String.format("%.1f", opt1)}, Opt2: ${String.format("%.1f", opt2)})"
            }
            SubjectFormulaType.DIPLOMA_BA -> {
                // Qz = 2*(0.7*Max(Qz1, Qz2) + 0.3*Min(Qz1, Qz2)), A = Best 2 of 3 (each out of 10) -> total 20, F = min(40, F)
                val qz = 2f * (0.7f * (q1 * 0.2f) + 0.3f * (q2 * 0.2f))
                val a = min(20f, (gaa / 100f) * 20f)
                val finalCap = min(40f, (f / 100f) * 45f)
                total = qz + a + finalCap
                formulaExplanation = "Qz (In-center Quiz): ${String.format("%.1f", qz)} + Assignments A (best 2 of 3): ${String.format("%.1f", a)} + End-Term F (capped at 40): ${String.format("%.1f", finalCap)}\n" +
                        "Sep 2026 rule: Must score F >= 10/40 and T >= 40 to pass!"
            }
            SubjectFormulaType.DIPLOMA_TDS -> {
                // T = 0.2 GAA + 0.2 ROE + 0.2 P1 + 0.2 P2 + 0.2 F
                total = 0.20f * gaa + 0.20f * pe1 + 0.20f * proj + 0.20f * proj + 0.20f * f
                formulaExplanation = "0.2*GAA + 0.2*ROE (Nov 29) + 0.2*Project1 + 0.2*Project2 + 0.2*EndTerm (Jan 10)"
            }
            SubjectFormulaType.DIPLOMA_PDSA -> {
                // T = 0.05GAA + 0.2OP + 0.45F + max (0.2max(Qz1, Qz2), (0.10Qz1+0.20Qz2 ))
                val gaaPart = 0.05f * gaa
                val opPart = 0.20f * pe1
                val fPart = 0.45f * f
                val quizPart = max(0.20f * max(q1, q2), 0.10f * q1 + 0.20f * q2)
                total = gaaPart + opPart + fPart + quizPart
                formulaExplanation = "0.05*GAA (${String.format("%.1f", gaaPart)}) + 0.2*OP (${String.format("%.1f", opPart)}) + 0.45*F (${String.format("%.1f", fPart)}) + Quiz Part (${String.format("%.1f", quizPart)})"
            }
            SubjectFormulaType.DIPLOMA_DBMS -> {
                // T = 0.03GAA2 + 0.02GAA3 + 0.2 OPPE + 0.45F + max (0.2max(Qz1, Qz2), (0.10Qz1+0.20Qz2 ))
                val gaaPart = 0.05f * gaa
                val opPart = 0.20f * pe1
                val fPart = 0.45f * f
                val quizPart = max(0.20f * max(q1, q2), 0.10f * q1 + 0.20f * q2)
                total = gaaPart + opPart + fPart + quizPart
                formulaExplanation = "0.03*SQL + 0.02*PythonDB (${String.format("%.1f", gaaPart)}) + 0.2*OPPE (${String.format("%.1f", opPart)}) + 0.45*F + Quiz Part (${String.format("%.1f", quizPart)})\n" +
                        "Sep 2026: Min 35% in OPPE + Python-DB connectivity correct mandatory!"
            }
            SubjectFormulaType.DIPLOMA_JAVA -> {
                // T = 0.05GAA + 0.2 of Max(PE1,PE2) + 0.45F + max (0.2max(Qz1, Qz2), (0.10Qz1+0.20Qz2 )) + (Bonus) 0.10 of Min(PE1,PE2)
                val gaaPart = 0.05f * gaa
                val peMax = 0.20f * max(pe1, pe2)
                val peMinBonus = 0.10f * min(pe1, pe2)
                val fPart = 0.45f * f
                val quizPart = max(0.20f * max(q1, q2), 0.10f * q1 + 0.20f * q2)
                total = gaaPart + peMax + fPart + quizPart + peMinBonus
                formulaExplanation = "0.05*GAA + 0.2*max(PE1,PE2) (${String.format("%.1f", peMax)}) + 0.45*F + Quiz Part (${String.format("%.1f", quizPart)}) + Min PE Bonus (${String.format("%.1f", peMinBonus)})"
            }
            SubjectFormulaType.DIPLOMA_SYS_COMMANDS -> {
                // T = 0.05 GAA + 0.25 Qz1 + 0.3 OPPE + 0.3F + 0.1 BPTA
                val gaaPart = 0.05f * gaa
                val qzPart = 0.25f * q1
                val oppePart = 0.30f * pe1
                val fPart = 0.30f * f
                val bptaPart = 0.10f * proj
                total = gaaPart + qzPart + oppePart + fPart + bptaPart
                formulaExplanation = "0.05*GAA + 0.25*Q1 (${String.format("%.1f", qzPart)}) + 0.3*OPPE (${String.format("%.1f", oppePart)}) + 0.3*F + 0.1*BPTA (${String.format("%.1f", bptaPart)})\n" +
                        "Sep 2026: OPPE (Dec 20 / Jan 3) score >= 40 required to pass!"
            }
            SubjectFormulaType.DIPLOMA_APP_DEV1 -> {
                // T = 0.05 GLA + max (0.6F + 0.25max(Qz1, Qz2), 0.4F + 0.25Qz1 + 0.3Qz2)
                val glaPart = 0.05f * gaa
                val opt1 = 0.60f * f + 0.25f * max(q1, q2)
                val opt2 = 0.40f * f + 0.25f * q1 + 0.30f * q2
                total = glaPart + max(opt1, opt2)
                formulaExplanation = "0.05*GLA (Lab Assignments) + max(Opt1: ${String.format("%.1f", opt1)}, Opt2: ${String.format("%.1f", opt2)})"
            }
            SubjectFormulaType.DIPLOMA_DL_GENAI -> {
                // T = 0.1 GAA + 0.2 Qz1 + 0.2 Qz2 + 0.25 F + 0.1 NPPE1 + 0.15 NPPE2
                val gaaPart = 0.10f * gaa
                val qzPart = 0.20f * q1 + 0.20f * q2
                val fPart = 0.25f * f
                val nppePart = 0.10f * pe1 + 0.15f * pe2
                total = gaaPart + qzPart + fPart + nppePart
                formulaExplanation = "0.1*GAA + 0.2*Q1 + 0.2*Q2 + 0.25*F + 0.1*NPPE1 (Nov 21) + 0.15*NPPE2 (Dec 11)"
            }
            SubjectFormulaType.DEGREE_SOFTWARE_TESTING, SubjectFormulaType.DEGREE_STANDARD -> {
                // T = 0.1GAA + 0.4F + 0.25Qz1 + 0.25Qz2
                val gaaPart = 0.10f * gaa
                val fPart = 0.40f * f
                val qzPart = 0.25f * q1 + 0.25f * q2
                total = gaaPart + fPart + qzPart
                formulaExplanation = "0.1*GAA (${String.format("%.1f", gaaPart)}) + 0.4*F (${String.format("%.1f", fPart)}) + 0.25*Q1 + 0.25*Q2 (${String.format("%.1f", qzPart)})"
            }
            SubjectFormulaType.DEGREE_SOFTWARE_ENG -> {
                // T = 0.05GAA + 0.2Qz2 + 0.4F+ 0.1GP1 + 0.1GP2 + 0.1PP + 0.05CP
                total = 0.05f * gaa + 0.20f * q2 + 0.40f * f + 0.35f * proj
                formulaExplanation = "0.05*GAA + 0.2*Q2 + 0.4*F + 0.35*Group Project Milestones & Presentation"
            }
            SubjectFormulaType.DEGREE_DEEP_LEARNING, SubjectFormulaType.DEGREE_AI_SEARCH, SubjectFormulaType.DEGREE_DL_CV, SubjectFormulaType.DEGREE_LLM, SubjectFormulaType.DEGREE_ADS -> {
                val gaaPart = 0.10f * gaa
                val fPart = 0.40f * f
                val qzPart = 0.25f * q1 + 0.25f * q2
                val base = gaaPart + fPart + qzPart
                val bonusPts = if (base >= 40f) min(bonus, 5f) else 0f
                total = min(100f, base + bonusPts)
                formulaExplanation = "Exams & Assignments: ${String.format("%.1f", base)} + Bonus: +${String.format("%.1f", bonusPts)} (applied on passing)"
            }
            SubjectFormulaType.DEGREE_SPG -> {
                // Sep 2026: T = 0.10*GAA + 0.25*GP + 0.30*Qz2 + 0.35*F
                total = 0.10f * gaa + 0.25f * proj + 0.30f * q2 + 0.35f * f
                formulaExplanation = "0.10*GAA + 0.25*Group Project + 0.30*Quiz2 (Dec 5) + 0.35*EndTerm (Jan 10)"
            }
            SubjectFormulaType.DEGREE_PROG_C -> {
                // T = 0.10GAA + 0.20Qz1 + 0.20 OPPE1 + 0.20 OPPE2 + 0.30F
                total = 0.10f * gaa + 0.20f * q1 + 0.20f * pe1 + 0.20f * pe2 + 0.30f * f
                formulaExplanation = "0.1*GAA + 0.2*Quiz1 (Nov 15) + 0.2*OPPE1 (Nov 22) + 0.2*OPPE2 (Dec 20) + 0.3*EndTerm (Jan 10)\n" +
                        "Sep 2026: Must score >= 40 in at least one OPPE!"
            }
            SubjectFormulaType.DEGREE_BIG_DATA -> {
                // Sep 2026: T = 0.1 GAA + 0.3 F + 0.2 OPPE1 + 0.4 OPPE2 + Bonus (5 marks)
                val base = 0.10f * gaa + 0.30f * f + 0.20f * pe1 + 0.40f * pe2
                val bonusPts = min(bonus, 5f)
                total = min(100f, base + bonusPts)
                formulaExplanation = "0.1*GAA + 0.3*F + 0.2*OPPE1 (Nov 22) + 0.4*OPPE2 (Dec 20) + Live Attendance Bonus (${String.format("%.1f", bonusPts)})"
            }
            SubjectFormulaType.DEGREE_MATH_GENAI -> {
                // Sep 2026: T = 0.05*GAA + 0.4*F + 0.2*Qz1 + 0.2*Qz2 + 0.15*NPPE
                total = 0.05f * gaa + 0.40f * f + 0.20f * q1 + 0.20f * q2 + 0.15f * proj
                formulaExplanation = "0.05*GAA + 0.4*F + 0.2*Quiz1 + 0.2*Quiz2 + 0.15*NPPE (Dec 11-14)"
            }
            SubjectFormulaType.DEGREE_MATH_THINKING -> {
                // T = 0.1GAA + max (0.6F + 0.2max(Qz1, Qz2), 0.4F + 0.2Qz1 + 0.3Qz2)
                val opt1 = 0.60f * f + 0.20f * max(q1, q2)
                val opt2 = 0.40f * f + 0.20f * q1 + 0.30f * q2
                total = 0.10f * gaa + max(opt1, opt2)
                formulaExplanation = "0.1*GAA + max(Opt1: ${String.format("%.1f", opt1)}, Opt2: ${String.format("%.1f", opt2)})"
            }
            SubjectFormulaType.DEGREE_SPEECH_TECH -> {
                // T = 0.15GAA + 0.15V + 0.3F + 0.20Qz1 + 0.20Qz2
                total = 0.15f * gaa + 0.15f * proj + 0.30f * f + 0.20f * q1 + 0.20f * q2
                formulaExplanation = "0.15*GAA + 0.15*Viva + 0.3*EndTerm + 0.2*Quiz1 + 0.2*Quiz2"
            }
            SubjectFormulaType.DEGREE_INDUSTRY_4 -> {
                // 15 marks from 2 quizzes + 5 marks from Online game + 40 marks from best 2 assignments + 30 marks from ET exam + 10 marks from project
                val qzPart = 0.15f * ((q1 + q2) / 2f)
                val gamePart = 5f
                val asgPart = (gaa / 100f) * 40f
                val etPart = (f / 100f) * 30f
                val projPart = (proj / 100f) * 10f
                total = qzPart + gamePart + asgPart + etPart + projPart
                formulaExplanation = "15 (Quizzes) + 5 (Game) + 40 (Assignments) + 30 (EndTerm) + 10 (Project)"
            }
            SubjectFormulaType.DEGREE_FINANCIAL_FORENSICS -> {
                // T = 0.1GAA + 0.25Qz1 + 0.30GP1 + 0.35F
                total = 0.10f * gaa + 0.25f * q1 + 0.30f * proj + 0.35f * f
                formulaExplanation = "0.1*GAA + 0.25*Quiz1 + 0.30*GroupProject + 0.35*EndTerm"
            }
            SubjectFormulaType.DEGREE_LINEAR_MODELS -> {
                // T = 0.1 GAA + 0.25*P + max(0.35 F + 0.20 max(Qz1,Qz2), 0.35F + 0.15Qz1 + 0.15Qz2)
                val opt1 = 0.35f * f + 0.20f * max(q1, q2)
                val opt2 = 0.35f * f + 0.15f * q1 + 0.15f * q2
                total = 0.10f * gaa + 0.25f * proj + max(opt1, opt2)
                formulaExplanation = "0.1*GAA + 0.25*Project + max(Opt1: ${String.format("%.1f", opt1)}, Opt2: ${String.format("%.1f", opt2)})"
            }
            SubjectFormulaType.DEGREE_REINFORCEMENT_LEARNING -> {
                // T = 0.05 GAA + 0.25 GPA + 0.2Qz1 + 0.2Qz2 + 0.3F + BONUS
                val base = 0.05f * gaa + 0.25f * proj + 0.20f * q1 + 0.20f * q2 + 0.30f * f
                val bonusPts = min(bonus, 5f)
                total = min(100f, base + bonusPts)
                formulaExplanation = "0.05*GAA + 0.25*GPA + 0.2*Quiz1 + 0.2*Quiz2 + 0.3*EndTerm + Bonus (+${String.format("%.1f", bonusPts)})"
            }
            SubjectFormulaType.DEGREE_CORPORATE_FINANCE -> {
                // T = 0.1GAA + 0.4F + 0.2Qz1 + 0.3Qz2
                total = 0.10f * gaa + 0.40f * f + 0.20f * q1 + 0.30f * q2
                formulaExplanation = "0.1*GAA + 0.4*EndTerm + 0.2*Quiz1 + 0.3*Quiz2"
            }
            SubjectFormulaType.DEGREE_COMPUTER_NETWORKS -> {
                // T = 0.1GAA + 0.30F + 0.25Qz1 + 0.25Qz2 + 0.1 Programming assignment
                total = 0.10f * gaa + 0.30f * f + 0.25f * q1 + 0.25f * q2 + 0.10f * proj
                formulaExplanation = "0.1*GAA + 0.3*EndTerm + 0.25*Quiz1 + 0.25*Quiz2 + 0.1*ProgAssignment"
            }
            SubjectFormulaType.DEGREE_DIGITAL_IC -> {
                // T = 0.1GAA + 0.3P + max(0.3F+0.2max(Qz1,Qz2), 0.3F+0.15Qz1+0.15Qz2)
                val opt1 = 0.30f * f + 0.20f * max(q1, q2)
                val opt2 = 0.30f * f + 0.15f * q1 + 0.15f * q2
                total = 0.10f * gaa + 0.30f * proj + max(opt1, opt2)
                formulaExplanation = "0.1*GAA + 0.3*CourseProject + max(Opt1: ${String.format("%.1f", opt1)}, Opt2: ${String.format("%.1f", opt2)})"
            }
            SubjectFormulaType.DEGREE_E_TESTING, SubjectFormulaType.DEGREE_COMM_SYSTEMS -> {
                // T = 0.1GAA + max(0.6F+0.2max(Qz1,Qz2), 0.4-0.5F+0.2Qz1+0.2-0.3Qz2)
                val opt1 = 0.60f * f + 0.20f * max(q1, q2)
                val opt2 = 0.45f * f + 0.20f * q1 + 0.25f * q2
                total = 0.10f * gaa + max(opt1, opt2)
                formulaExplanation = "0.1*GAA + max(Opt1: ${String.format("%.1f", opt1)}, Opt2: ${String.format("%.1f", opt2)})"
            }
            SubjectFormulaType.DEGREE_DIGITAL_SYS_DESIGN, SubjectFormulaType.DEGREE_COMP_ORG -> {
                // T = 0.05 GAA + max(0.6F + 0.25max(Qz1,Qz2), 0.5F + 0.2Qz1 + 0.25Qz2)
                val opt1 = 0.60f * f + 0.25f * max(q1, q2)
                val opt2 = 0.50f * f + 0.20f * q1 + 0.25f * q2
                total = 0.05f * gaa + max(opt1, opt2)
                formulaExplanation = "0.05*GAA + max(Opt1: ${String.format("%.1f", opt1)}, Opt2: ${String.format("%.1f", opt2)})"
            }
            SubjectFormulaType.DEGREE_MLOPS -> {
                // T = 0.2GAA + 0.3F + 0.25 OPPE1 + 0.25 OPPE2 + Bonus
                val base = 0.20f * gaa + 0.30f * f + 0.25f * pe1 + 0.25f * pe2
                total = min(100f, base + bonus)
                formulaExplanation = "0.2*GAA + 0.3*EndTerm + 0.25*OPPE1 (Nov 22) + 0.25*OPPE2 (Jan 3) + Active Course Participation Bonus"
            }
            else -> {
                // Standard default
                val opt1 = 0.60f * f + 0.30f * max(q1, q2)
                val opt2 = 0.45f * f + 0.25f * q1 + 0.30f * q2
                total = max(opt1, opt2)
                formulaExplanation = "Standard Formula: max(0.6F + 0.3max(Q1,Q2), 0.45F + 0.25Q1 + 0.3Q2)"
            }
        }

        total = total.coerceIn(0f, 100f)

        // Grade determination
        val letterGrade = when {
            !inputs.attendedFinal && total >= 35f -> "I"
            subject.hasOppe && max(pe1, pe2) < 40f && total >= 40f -> "I_OP"
            total >= 90f -> "S"
            total >= 80f -> "A"
            total >= 70f -> "B"
            total >= 60f -> "C"
            total >= 50f -> "D"
            total >= 40f -> "E"
            else -> "U"
        }

        val isPass = letterGrade in listOf("S", "A", "B", "C", "D", "E")

        val eligibilityNote = buildString {
            if (isEligibleFinal) {
                append("✓ Eligible for End-Term Exam (GAA >= 40 & Quiz attendance). ")
            } else {
                append("⚠ Ineligible for End-Term: Needs GAA >= 40 in best 5 of first 7 weeks. ")
            }
            if (subject.hasOppe) {
                if (isEligibleOppe) {
                    append("✓ SCT Exam cleared for OPPE. ")
                } else {
                    append("⚠ SCT Exam must be completed for OPPE scheduling. ")
                }
            }
        }

        return GradeResult(
            totalScore = total,
            letterGrade = letterGrade,
            isEligibleForFinal = isEligibleFinal,
            isEligibleForOppe = isEligibleOppe,
            isPass = isPass,
            breakdownText = formulaExplanation,
            eligibilityNote = eligibilityNote
        )
    }
}
