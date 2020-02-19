package com.pechuro.bsuirschedule.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.pechuro.bsuirschedule.common.factory.ChildWorkerFactory
import com.pechuro.bsuirschedule.domain.common.getOrDefault
import com.pechuro.bsuirschedule.domain.interactor.GetAvailableForUpdateSchedules
import com.pechuro.bsuirschedule.domain.interactor.GetAvailableForUpdateSchedules.Params
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UpdateScheduleWorker(
        context: Context,
        params: WorkerParameters,
        private val getAvailableForUpdateSchedules: GetAvailableForUpdateSchedules
) : CoroutineWorker(context, params) {

    companion object {
        const val TAG = "UpdateScheduleWorker"
    }

    override suspend fun doWork(): Result {
        getAvailableForUpdateSchedules.execute(Params(includeAll = false))
                .getOrDefault(emptyFlow())
                .debounce(5000)
                .first()
                .forEach {

                }
        return Result.success()
    }

    class Factory @Inject constructor(
            private val getAvailableForUpdateSchedules: GetAvailableForUpdateSchedules
    ) : ChildWorkerFactory {

        override fun create(context: Context, params: WorkerParameters) = UpdateScheduleWorker(
                context = context,
                params = params,
                getAvailableForUpdateSchedules = getAvailableForUpdateSchedules
        )
    }
}