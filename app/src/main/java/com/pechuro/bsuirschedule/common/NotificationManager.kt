package com.pechuro.bsuirschedule.common

import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.PendingIntent
import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bsuir.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.ext.doIfVersionGreaterThan
import com.pechuro.bsuirschedule.feature.MainActivity
import javax.inject.Inject

class NotificationManager @Inject constructor(
    private val context: Context
) {

    companion object {
        private const val GROUP_KEY_ACTIONS = "GROUP_ID_ACTIONS"

        private const val CHANNEL_ID_UPDATE_SCHEDULE = "CHANNEL_ID_UPDATE_SCHEDULE"
    }

    private val notificationManager = NotificationManagerCompat.from(context)

    init {
        createNotificationActionGroup()
        createUpdateScheduleChannel()
    }

    fun showUpdateAvailable(schedule: Schedule) {
        val notificationId = getUpdateAvailableNotificationId(schedule)
        val title = context.getString(R.string.notification_update_title, schedule.name)
        val description = context.getString(R.string.notification_update_description)
        val intentFlags = if (SDK_INT >= VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        val intent = PendingIntent.getActivity(
            context,
            0,
            MainActivity.newIntent(context),
            intentFlags
        )
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_UPDATE_SCHEDULE)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.app_icon_foreground)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setGroup(GROUP_KEY_ACTIONS)
            .setContentIntent(intent)
            .build()
        notificationManager.notify(notificationId, notification)
    }

    fun dismissUpdateAvailable(schedule: Schedule) {
        val notificationId = getUpdateAvailableNotificationId(schedule)
        notificationManager.cancel(notificationId)
    }

    private fun getUpdateAvailableNotificationId(schedule: Schedule) = schedule.name.hashCode()

    private fun createNotificationActionGroup() {
        doIfVersionGreaterThan(VERSION_CODES.O) {
            val groupId = GROUP_KEY_ACTIONS
            val groupName = context.getString(R.string.notification_group_actions_label)
            val channelGroup = NotificationChannelGroup(groupId, groupName)
            notificationManager.createNotificationChannelGroup(channelGroup)
        }
    }

    private fun createUpdateScheduleChannel() {
        doIfVersionGreaterThan(VERSION_CODES.O) {
            val name = context.getString(R.string.notification_channel_update_label)
            val importance = IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID_UPDATE_SCHEDULE, name, importance).apply {
                group = GROUP_KEY_ACTIONS
            }
            notificationManager.createNotificationChannel(channel)
        }
    }
}