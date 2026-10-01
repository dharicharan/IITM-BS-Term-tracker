package com.example.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.MainActivity
import com.example.data.model.Assignment
import com.example.data.model.Priority
import java.util.concurrent.TimeUnit

object ReminderManager {

    const val CHANNEL_ID = "assignments_reminder_channel"
    private const val CHANNEL_NAME = "Assignment & Deadline Reminders"
    private const val CHANNEL_DESCRIPTION = "Alerts for upcoming academic assignments, quizzes, and OPPEs"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
                enableVibration(true)
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun hasNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    fun sendNotification(
        context: Context,
        title: String,
        message: String,
        notificationId: Int = (System.currentTimeMillis() % 10000).toInt()
    ) {
        if (!hasNotificationPermission(context)) return

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        try {
            with(NotificationManagerCompat.from(context)) {
                notify(notificationId, builder.build())
            }
        } catch (_: SecurityException) {
            // Permission revoked
        }
    }

    fun checkAndTriggerAutomatedReminders(context: Context, activeAssignments: List<Assignment>): Int {
        val now = System.currentTimeMillis()
        var triggeredCount = 0

        for (assignment in activeAssignments) {
            if (!assignment.reminderEnabled) continue

            val diff = assignment.dueTimestamp - now
            val hoursLeft = TimeUnit.MILLISECONDS.toHours(diff)

            // Trigger notification for assignments due in <= 48 hours or critical upcoming tasks
            if (hoursLeft in 0..48 || assignment.priority == Priority.CRITICAL && hoursLeft in 0..72) {
                val timeString = if (hoursLeft < 1) "in less than an hour!" else "in $hoursLeft hours"
                val priorityPrefix = when (assignment.priority) {
                    Priority.CRITICAL -> "🚨 [CRITICAL DEADLINE]"
                    Priority.HIGH -> "⚠️ [High Priority]"
                    else -> "📌 [Reminder]"
                }
                sendNotification(
                    context = context,
                    title = "$priorityPrefix ${assignment.subjectName}",
                    message = "${assignment.title} is due $timeString (${assignment.dueDate}). Don't forget to submit!",
                    notificationId = assignment.id.toInt()
                )
                triggeredCount++
                if (triggeredCount >= 3) break // don't overwhelm user at once
            }
        }
        return triggeredCount
    }

    fun sendTestNotification(context: Context) {
        sendNotification(
            context = context,
            title = "🔔 TermTracker: Notification Test",
            message = "Automated deadline reminders are active! You will be notified ahead of upcoming assignments, quizzes, and OPPEs.",
            notificationId = 9999
        )
    }
}
