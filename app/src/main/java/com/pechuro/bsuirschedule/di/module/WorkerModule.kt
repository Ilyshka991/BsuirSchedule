package com.pechuro.bsuirschedule.di.module

import com.pechuro.bsuirschedule.common.factory.ChildWorkerFactory
import com.pechuro.bsuirschedule.common.factory.WorkerFactory
import com.pechuro.bsuirschedule.di.annotations.WorkerKey
import com.pechuro.bsuirschedule.worker.UpdateScheduleWorker
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface WorkerModule {

    @Binds
    fun bindWorkerFactory(factory: WorkerFactory): androidx.work.WorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(UpdateScheduleWorker::class)
    fun updateSchedule(viewModel: UpdateScheduleWorker.Factory): ChildWorkerFactory
}