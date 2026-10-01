package com.example.data.model

enum class CourseLevel(val displayName: String) {
    FOUNDATION("Foundation Level"),
    DIPLOMA("Diploma Level"),
    DEGREE("Degree Level"),
    CUSTOM("Custom")
}

enum class Priority(val displayName: String, val level: Int) {
    CRITICAL("Critical", 4),
    HIGH("High", 3),
    MEDIUM("Medium", 2),
    LOW("Low", 1)
}

enum class AssignmentType(val displayName: String) {
    WEEKLY_GA("Weekly Graded (GA)"),
    PROGRAMMING("Programming (GrPA)"),
    QUIZ("Quiz"),
    OPPE("OPPE Exam"),
    PROJECT("Project / Lab"),
    EXTRA_ACTIVITY("Bonus / Activity"),
    CUSTOM("Other Assessment")
}

enum class AssignmentStatus(val displayName: String) {
    PENDING("Pending"),
    IN_PROGRESS("In Progress"),
    SUBMITTED("Submitted")
}

enum class SubjectFormulaType {
    FOUNDATION_STANDARD,        // max(0.6F + 0.3max(Q1,Q2), 0.45F + 0.25Q1 + 0.3Q2)
    FOUNDATION_STATS1,          // max(...) + Bonus(capped 5)
    FOUNDATION_MATH2,           // max(...) + Bonus(capped 6)
    FOUNDATION_PYTHON,          // 0.15Q1 + 0.4F + 0.25max(PE1,PE2) + 0.2min(PE1,PE2)
    DIPLOMA_MLF,                // max(0.6F + 0.3max(Q1,Q2), 0.45F + 0.25Q1 + 0.3Q2) + BONUS (Sep 2026 pattern)
    DIPLOMA_MLT,                // 0.05GAA + max(...) + Bonus(3)
    DIPLOMA_MLP,                // 0.05GAA + 0.40F + 0.20OPPE1 + 0.20OPPE2 + 0.15KA (Sep 2026 pattern)
    DIPLOMA_BDM,                // 0.05GAA + max(...)
    DIPLOMA_BA,                 // Qz + A + F
    DIPLOMA_TDS,                // 0.2GAA + 0.2ROE + 0.2P1 + 0.2P2 + 0.2F
    DIPLOMA_PDSA,               // 0.05GAA + 0.2OP + 0.45F + max(0.2max(Q1,Q2), 0.1Q1+0.2Q2)
    DIPLOMA_DBMS,               // 0.03GAA2 + 0.02GAA3 + 0.2OPPE + 0.45F + max(0.2max(Q1,Q2), 0.1Q1+0.2Q2)
    DIPLOMA_JAVA,               // 0.05GAA + 0.2max(PE1,PE2) + 0.45F + max(...) + 0.1min(PE1,PE2)
    DIPLOMA_SYS_COMMANDS,       // 0.05GAA + 0.25Q1 + 0.3OPPE + 0.3F + 0.1BPTA
    DIPLOMA_APP_DEV1,           // 0.05GLA + max(0.6F + 0.25max(Q1,Q2), 0.4F + 0.25Q1 + 0.3Q2)
    DIPLOMA_APP_DEV2,           // 0.05GAA + max(0.6F + 0.25max(Q1,Q2), 0.4F + 0.25Q1 + 0.3Q2)
    DIPLOMA_DL_GENAI,           // 0.1GAA + 0.2Q1 + 0.2Q2 + 0.25F + 0.1NPPE1 + 0.15NPPE2
    DEGREE_SOFTWARE_TESTING,    // 0.1GAA + 0.4F + 0.25Q1 + 0.25Q2
    DEGREE_SOFTWARE_ENG,        // 0.05GAA + 0.2Q2 + 0.4F + 0.1GP1 + 0.1GP2 + 0.1PP + 0.05CP
    DEGREE_DEEP_LEARNING,       // 0.05GAA + 0.25Q1 + 0.25Q2 + 0.45F + Bonus
    DEGREE_AI_SEARCH,           // 0.1GAA + 0.4F + 0.25Q1 + 0.25Q2 + Bonus
    DEGREE_SPG,                 // 0.10*GAA + 0.25*GP + 0.30*Qz2 + 0.35*F (Sep 2026 pattern)
    DEGREE_PROG_C,              // 0.1GAA + 0.2Q1 + 0.2OPPE1 + 0.2OPPE2 + 0.3F
    DEGREE_DL_CV,               // 0.1GAA + 0.4F + 0.25Q1 + 0.25Q2 + Bonus
    DEGREE_LLM,                 // 0.05GAA + 0.35F + 0.3Q1 + 0.3Q2 + Bonus
    DEGREE_DL_PRACTICE,         // 0.05GA + 0.15Q1 + 0.15Q2 + 0.15Q3 + 0.25NPPE + 0.25Viva
    DEGREE_DSAI_LAB,            // 0.05GAA + 0.25Q2 + 0.4P + 0.3V + Bonus
    DEGREE_APPDEV_LAB,          // 0.2Q2 + 0.3GAA + 0.5Viva
    DEGREE_BIOINFORMATICS,      // 0.1GAA + 0.4F + 0.25Q1 + 0.25Q2
    DEGREE_INDUSTRY_4,          // 15 (Qz) + 5 (Game) + 40 (Asg) + 30 (ET) + 10 (P)
    DEGREE_MATH_THINKING,       // 0.1GAA + max(0.6F + 0.2max(Q1,Q2), 0.4F + 0.2Q1 + 0.3Q2)
    DEGREE_MLOPS,               // 0.2GAA + 0.3F + 0.25OPPE1 + 0.25OPPE2 + Bonus
    DEGREE_MATH_GENAI,          // 0.05GAA + 0.4F + 0.2Q1 + 0.2Q2 + 0.15NPPE (Sep 2026 pattern)
    DEGREE_SPEECH_TECH,         // 0.15GAA + 0.15V + 0.3F + 0.20Q1 + 0.20Q2
    DEGREE_DESIGN_THINKING,     // 0.1GAA + 0.1GP1 + 0.1GP2 + 0.2GP3 + 0.2Q2 + 0.3F
    DEGREE_FINANCIAL_FORENSICS, // 0.1GAA + 0.25Q1 + 0.30GP1 + 0.35F
    DEGREE_BIG_DATA,            // 0.1GAA + 0.3F + 0.2OPPE1 + 0.4OPPE2 + Bonus
    DEGREE_LINEAR_MODELS,       // 0.1GAA + 0.25P + max(0.35F + 0.2max(Q1,Q2), 0.35F + 0.15Q1 + 0.15Q2)
    DEGREE_REINFORCEMENT_LEARNING, // 0.05GAA + 0.25GPA + 0.2Q1 + 0.2Q2 + 0.3F + BONUS
    DEGREE_CORPORATE_FINANCE,   // 0.1GAA + 0.4F + 0.2Q1 + 0.3Q2
    DEGREE_COMPUTER_NETWORKS,   // 0.1GAA + 0.30F + 0.25Q1 + 0.25Q2 + 0.1ProgAssign
    DEGREE_COMPUTER_SYSTEMS,    // 0.1GAA + 0.4F + 0.2Q1 + 0.25Q2 + 0.05CircuitVerse
    DEGREE_GAME_THEORY,         // 0.1GAA + 0.4F + 0.25Q1 + 0.25Q2
    DEGREE_ADS,                 // 0.1GAA + 0.4F + 0.25Q1 + 0.25Q2 + BONUS
    DEGREE_DIGITAL_IC,          // 0.1GAA + 0.3P + max(0.3F+0.2max(Q1,Q2), 0.3F+0.15Q1+0.15Q2)
    DEGREE_E_TESTING,           // 0.1GAA + max(0.6F+0.2max(Q1,Q2), 0.5F+0.2Q1+0.2Q2)
    DEGREE_COMM_SYSTEMS,        // 0.1GAA + max(0.6F+0.2max(Q1,Q2), 0.4F+0.2Q1+0.3Q2)
    DEGREE_DIGITAL_SYS_DESIGN,  // 0.05GAA + max(0.6F+0.25max(Q1,Q2), 0.5F+0.2Q1+0.25Q2)
    DEGREE_COMP_ORG,            // 0.05GAA + max(0.6F+0.25max(Q1,Q2), 0.5F+0.2Q1+0.25Q2)
    DEGREE_STANDARD             // 0.1GAA + 0.4F + 0.25Q1 + 0.25Q2
}
