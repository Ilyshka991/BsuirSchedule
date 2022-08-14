package com.pechuro.bsuirschedule.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
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
import java.util.Calendar
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

        private const val SCHEDULE_AT_HOUR = 20
        private const val PERIOD_HOURS = 24

        fun scheduleNextWork(context: Context) {
            val workRequest = createOneTimeRequestRequest()
            WorkManager.getInstance(context).enqueueUniqueWork(
                TAG,
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
        }

        private fun createOneTimeRequestRequest(): OneTimeWorkRequest {
            val currentDate = Calendar.getInstance()
            val dueDate = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, SCHEDULE_AT_HOUR)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }

            if (dueDate.before(currentDate)) {
                dueDate.add(Calendar.HOUR_OF_DAY, PERIOD_HOURS)
            }
            val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis

            return OneTimeWorkRequestBuilder<CheckScheduleUpdatesWorker>()
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
        scheduleNextWork(applicationContext)
        return Result.success()
    }

    class Factory @Inject constructor(
        private val getAvailableForUpdateSchedules: GetAvailableForUpdateSchedules,
        private val notificationManager: NotificationManager
    ) : ChildWorkerFactory {

        override fun create(context: Context, params: WorkerParameters) =
            CheckScheduleUpdatesWorker(
                context = context,
                params = params,
                getAvailableForUpdateSchedules = getAvailableForUpdateSchedules,
                notificationManager = notificationManager
            )
    }
}