package com.example.ui.dashboard

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DonutLarge
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.Assignment
import com.example.data.model.AssignmentStatus
import com.example.data.model.Subject
import kotlin.math.min

data class SubjectProgress(
    val subject: Subject,
    val totalCount: Int,
    val completedCount: Int,
    val percentage: Float
)

enum class ChartStyle {
    BARS,
    DONUT
}

@Composable
fun SubjectCompletionChart(
    enrolledSubjects: List<Subject>,
    allAssignments: List<Assignment>,
    selectedSubjectFilter: String?,
    onSelectSubject: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    if (enrolledSubjects.isEmpty()) return

    // Calculate completion stats per enrolled subject
    val progressList = remember(enrolledSubjects, allAssignments) {
        enrolledSubjects.map { subject ->
            val subjectAssignments = allAssignments.filter { it.subjectId == subject.id }
            val total = subjectAssignments.size
            val completed = subjectAssignments.count { it.status == AssignmentStatus.SUBMITTED }
            val pct = if (total > 0) (completed.toFloat() / total.toFloat()) * 100f else 0f
            SubjectProgress(
                subject = subject,
                totalCount = total,
                completedCount = completed,
                percentage = pct
            )
        }
    }

    val totalAssignments = allAssignments.size
    val totalCompleted = allAssignments.count { it.status == AssignmentStatus.SUBMITTED }
    val overallPercentage = if (totalAssignments > 0) {
        (totalCompleted.toFloat() / totalAssignments.toFloat()) * 100f
    } else 0f

    var chartStyle by remember { mutableStateOf(ChartStyle.BARS) }

    // Animation progress (0f to 1f)
    val animationProgress = remember { Animatable(0f) }
    LaunchedEffect(progressList, chartStyle) {
        animationProgress.snapTo(0f)
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        )
    }

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("subject_completion_chart"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Row: Title, Overall Badge, View Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Subject Completion Rate",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$totalCompleted of $totalAssignments assignments completed (${String.format("%.0f", overallPercentage)}%)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { chartStyle = ChartStyle.BARS },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (chartStyle == ChartStyle.BARS) MaterialTheme.colorScheme.primaryContainer
                                else Color.Transparent
                            )
                            .testTag("chart_style_bars")
                    ) {
                        Icon(
                            Icons.Default.BarChart,
                            contentDescription = "Bar View",
                            tint = if (chartStyle == ChartStyle.BARS) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    IconButton(
                        onClick = { chartStyle = ChartStyle.DONUT },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (chartStyle == ChartStyle.DONUT) MaterialTheme.colorScheme.primaryContainer
                                else Color.Transparent
                            )
                            .testTag("chart_style_donut")
                    ) {
                        Icon(
                            Icons.Default.DonutLarge,
                            contentDescription = "Donut View",
                            tint = if (chartStyle == ChartStyle.DONUT) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            if (chartStyle == ChartStyle.BARS) {
                // Horizontal Bars Rendered on Canvas
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    progressList.forEach { item ->
                        val isSelected = selectedSubjectFilter == item.subject.id
                        val subjectColor = Color(item.subject.colorHex)

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    onSelectSubject(if (isSelected) null else item.subject.id)
                                }
                                .padding(vertical = 4.dp, horizontal = 2.dp)
                                .testTag("subject_progress_${item.subject.id}")
                        ) {
                            // Label row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .clip(CircleShape)
                                            .background(subjectColor)
                                    )
                                    Text(
                                        text = "${item.subject.code} - ${item.subject.name}",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "${item.completedCount}/${item.totalCount}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "${String.format("%.0f", item.percentage)}%",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = subjectColor
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Compose Canvas for smooth animated progress bar
                            val animFactor = animationProgress.value
                            val trackBgColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)

                            Canvas(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                            ) {
                                val canvasWidth = size.width
                                val canvasHeight = size.height
                                val cornerRadius = CornerRadius(canvasHeight / 2f, canvasHeight / 2f)

                                // Draw background track
                                drawRoundRect(
                                    color = trackBgColor,
                                    size = Size(canvasWidth, canvasHeight),
                                    cornerRadius = cornerRadius
                                )

                                // Draw progress bar with animation
                                val progressFraction = (item.percentage / 100f).coerceIn(0f, 1f) * animFactor
                                if (progressFraction > 0f) {
                                    drawRoundRect(
                                        color = subjectColor,
                                        size = Size(canvasWidth * progressFraction, canvasHeight),
                                        cornerRadius = cornerRadius
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // Donut Chart View using Compose Canvas
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    // Donut Canvas
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val animFactor = animationProgress.value
                        val trackBgColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)

                        Canvas(modifier = Modifier.size(130.dp)) {
                            val strokeWidth = 18.dp.toPx()
                            val diameter = min(size.width, size.height) - strokeWidth
                            val topLeft = Offset(
                                (size.width - diameter) / 2f,
                                (size.height - diameter) / 2f
                            )
                            val arcSize = Size(diameter, diameter)

                            // Background circle
                            drawArc(
                                color = trackBgColor,
                                startAngle = 0f,
                                sweepAngle = 360f,
                                useCenter = false,
                                topLeft = topLeft,
                                size = arcSize,
                                style = Stroke(width = strokeWidth)
                            )

                            // Segments for completed assignments per subject
                            if (totalAssignments > 0) {
                                var currentAngle = -90f
                                progressList.forEach { item ->
                                    val subjectShareSweep = (item.completedCount.toFloat() / totalAssignments.toFloat()) * 360f * animFactor
                                    if (subjectShareSweep > 0.5f) {
                                        drawArc(
                                            color = Color(item.subject.colorHex),
                                            startAngle = currentAngle,
                                            sweepAngle = subjectShareSweep,
                                            useCenter = false,
                                            topLeft = topLeft,
                                            size = arcSize,
                                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                                        )
                                        currentAngle += subjectShareSweep
                                    }
                                }
                            }
                        }

                        // Center overall percentage text
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "${String.format("%.0f", overallPercentage)}%",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Done",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Legend Column
                    Column(
                        modifier = Modifier.weight(1f).padding(start = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        progressList.forEach { item ->
                            val isSelected = selectedSubjectFilter == item.subject.id
                            val subjectColor = Color(item.subject.colorHex)

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable {
                                        onSelectSubject(if (isSelected) null else item.subject.id)
                                    }
                                    .padding(vertical = 2.dp, horizontal = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(subjectColor)
                                    )
                                    Text(
                                        text = item.subject.code,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1
                                    )
                                }

                                Text(
                                    text = "${String.format("%.0f", item.percentage)}% (${item.completedCount}/${item.totalCount})",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = subjectColor
                                )
                            }
                        }
                    }
                }
            }

            // Hint footer
            Text(
                text = if (selectedSubjectFilter != null) "Tip: Tapped filter active. Tap again to clear filter." else "Tip: Tap any subject bar to filter dashboard deadlines.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )
        }
    }
}
