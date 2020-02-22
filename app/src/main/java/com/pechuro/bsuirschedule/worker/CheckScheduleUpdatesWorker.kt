package com.pechuro.bsuirschedule.worker

import android.content.Context
import androidx.annotation.IntRange
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.pechuro.bsuirschedule.common.NotificationManager
import com.pechuro.bsuirschedule.common.factory.ChildWorkerFactory
import com.pechuro.bsuirschedule.domain.common.getOrDefault
import com.pechuro.bsuirschedule.domain.interactor.GetAvailableForUpdateSchedules
import com.pechuro.bsuirschedule.domain.interactor.GetAvailableForUpdateSchedules.Params
import com.pechuro.bsuirschedule.ext.app
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CheckScheduleUpdatesWorker(
        context: Context,
        params: WorkerParameters,
        private val getAvailableForUpdateSchedules: GetAvailableForUpdateSchedules,
        private val notificationManager: NotificationManager
) : CoroutineWorker(context, params) {

    companion object {
        const val TAG = "UpdateScheduleWorker"

        fun createPeriodicRequest(
                @IntRange(from = 0, to = 23) scheduleAtHour: Int,
                repeatIntervalMillis: Long
        ): PeriodicWorkRequest {
            val executeAtTimeMillis = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, scheduleAtHour)
                if (System.currentTimeMillis() > timeInMillis) {
                    add(Calendar.DATE, 1)
                }
            }.timeInMillis
            val timeDiff = executeAtTimeMillis - System.currentTimeMillis()
            return PeriodicWorkRequestBuilder<CheckScheduleUpdatesWorker>(repeatIntervalMillis, TimeUnit.MILLISECONDS)
                    .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                    .addTag(TAG)
                    .build()
        }
    }

    override suspend fun doWork(): Result {
        getAvailableForUpdateSchedules.execute(Params(includeAll = false))
                .getOrDefault(emptyFlow())
                .debounce(5000)
                .first()
                .distinctBy { it.name }
                .forEach {
                    if (!applicationContext.app.isInForeground) {
                        notificationManager.showUpdateAvailable(it)
                    }
                }
        return Result.success()
    }

    class Factory @Inject constructor(
            private val getAvailableForUpdateSchedules: GetAvailableForUpdateSchedules,
            private val notificationManager: NotificationManager
    ) : ChildWorkerFactory {

        override fun create(context: Context, params: WorkerParameters) = CheckScheduleUpdatesWorker(
                context = context,
                params = params,
                getAvailableForUpdateSchedules = getAvailableForUpdateSchedules,
                notificationManager = notificationManager
        )
    }
}