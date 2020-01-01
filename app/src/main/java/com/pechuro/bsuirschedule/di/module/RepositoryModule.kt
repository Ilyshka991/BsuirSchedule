package com.pechuro.bsuirschedule.di.module

import com.pechuro.bsuirschedule.data.repository.EmployeeRepositoryImpl
import com.pechuro.bsuirschedule.data.repository.GroupRepositoryImpl
import com.pechuro.bsuirschedule.data.repository.ScheduleRepositoryImpl
import com.pechuro.bsuirschedule.di.annotations.AppScope
import com.pechuro.bsuirschedule.domain.repository.IEmployeeRepository
import com.pechuro.bsuirschedule.domain.repository.IGroupRepository
import com.pechuro.bsuirschedule.domain.repository.IScheduleRepository
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    @AppScope
    fun provideEmployeeRepository(): IEmployeeRepository = EmployeeRepositoryImpl()

    @Provides
    @AppScope
    fun provideGroupRepository(): IGroupRepository = GroupRepositoryImpl()

    @Provides
    @AppScope
    fun provideScheduleRepository(): IScheduleRepository = ScheduleRepositoryImpl()
}