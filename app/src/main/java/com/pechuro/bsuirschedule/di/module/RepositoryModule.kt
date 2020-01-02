package com.pechuro.bsuirschedule.di.module

import com.pechuro.bsuirschedule.data.repository.*
import com.pechuro.bsuirschedule.di.annotations.AppScope
import com.pechuro.bsuirschedule.domain.repository.*
import com.pechuro.bsuirschedule.local.dao.BuildingDao
import com.pechuro.bsuirschedule.local.dao.SpecialityDao
import com.pechuro.bsuirschedule.remote.api.AnnouncementApi
import com.pechuro.bsuirschedule.remote.api.BuildingApi
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
    fun provideBuildingRepository(
            api: BuildingApi,
            buildingDao: BuildingDao,
            specialityDao: SpecialityDao
    ): IBuildingRepository = BuildingRepositoryImpl(
            api = api,
            buildingDao = buildingDao,
            specialityDao = specialityDao
    )

    @Provides
    @AppScope
    fun provideAnnouncementRepository(api: AnnouncementApi): IAnnouncementRepository =
            AnnouncementRepositoryImpl(api)
}