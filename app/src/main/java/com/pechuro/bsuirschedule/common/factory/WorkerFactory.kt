package com.pechuro.bsuirschedule.common.factory

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import javax.inject.Inject
import javax.inject.Provider

class WorkerFactory @Inject constructor(
    private val workerFactories: MutableMap<Class<out ListenableWorker>, Provider<ChildWorkerFactory>>
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? =
        workerFactories[Class.forName(workerClassName)]
            ?.get()
            ?.create(appContext, workerParameters)
            ?: throw IllegalArgumentException("No worker factory for $workerClassName found. Did you specify it in WorkerModule.kt?")
}