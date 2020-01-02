package com.pechuro.bsuirschedule.di.module

import com.pechuro.bsuirschedule.data.repository.*
import com.pechuro.bsuirschedule.di.annotations.AppScope
import com.pechuro.bsuirschedule.domain.repository.*
import com.pechuro.bsuirschedule.local.dao.BuildingDao
import com.pechuro.bsuirschedule.local.dao.EmployeeDao
import com.pechuro.bsuirschedule.local.dao.GroupDao
import com.pechuro.bsuirschedule.local.dao.SpecialityDao
import com.pechuro.bsuirschedule.remote.api.AnnouncementApi
import com.pechuro.bsuirschedule.remote.api.BuildingApi
import com.pechuro.bsuirschedule.remote.api.SpecialityApi
import com.pechuro.bsuirschedule.remote.api.StaffApi
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    @AppScope
    fun provideEmployeeRepository(
            api: StaffApi,
            dao: EmployeeDao
    ): IEmployeeRepository = EmployeeRepositoryImpl(
            api = api,
            dao = dao
    )

    @Provides
    @AppScope
    fun provideGroupRepository(
            api: StaffApi,
            groupDao: GroupDao,
            specialityDao: SpecialityDao
    ): IGroupRepository = GroupRepositoryImpl(
            api = api,
            groupDao = groupDao,
            specialityDao = specialityDao
    )

    @Provides
    @AppScope
    fun provideScheduleRepository(): IScheduleRepository = ScheduleRepositoryImpl()

    @Provides
    @AppScope
    fun provideSpecialityRepository(
            api: SpecialityApi,
            dao: SpecialityDao
    ): ISpecialityRepository = SpecialityRepositoryImpl(
            api = api,
            dao = dao
    )

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