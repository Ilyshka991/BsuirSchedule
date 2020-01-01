package com.pechuro.bsuirschedule.di.module

import com.pechuro.bsuirschedule.data.repository.*
import com.pechuro.bsuirschedule.di.annotations.AppScope
import com.pechuro.bsuirschedule.domain.repository.*
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

    @Provides
    @AppScope
    fun provideSpecialityRepository(): ISpecialityRepository = SpecialityRepositoryImpl()

    @Provides
    @AppScope
    fun provideBuildingRepository(): IBuildingRepository = BuildingRepositoryImpl()
}