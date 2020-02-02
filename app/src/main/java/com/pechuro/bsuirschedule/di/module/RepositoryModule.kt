package com.pechuro.bsuirschedule.di.module

import com.pechuro.bsuirschedule.data.repository.*
import com.pechuro.bsuirschedule.di.annotations.AppScope
import com.pechuro.bsuirschedule.domain.repository.*
import com.pechuro.bsuirschedule.local.dao.*
import com.pechuro.bsuirschedule.remote.api.*
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    @AppScope
    fun provideEmployeeRepository(
            api: StaffApi,
            dao: EmployeeDao,
            specialityRepository: ISpecialityRepository
    ): IEmployeeRepository = EmployeeRepositoryImpl(
            api = api,
            dao = dao,
            specialityRepository = specialityRepository
    )

    @Provides
    @AppScope
    fun provideGroupRepository(
            api: StaffApi,
            dao: GroupDao,
            specialityRepository: ISpecialityRepository
    ): IGroupRepository = GroupRepositoryImpl(
            api = api,
            dao = dao,
            specialityRepository = specialityRepository
    )

    @Provides
    @AppScope
    fun provideScheduleRepository(
            api: ScheduleApi,
            dao: ScheduleDao,
            groupRepository: IGroupRepository,
            buildingRepository: IBuildingRepository,
            specialityRepository: ISpecialityRepository
    ): IScheduleRepository = ScheduleRepositoryImpl(
            api = api,
            dao = dao,
            groupRepository = groupRepository,
            buildingRepository = buildingRepository,
            specialityRepository = specialityRepository
    )

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
            dao: BuildingDao,
            specialityRepository: ISpecialityRepository
    ): IBuildingRepository = BuildingRepositoryImpl(
            api = api,
            dao = dao,
            specialityRepository = specialityRepository
    )

    @Provides
    @AppScope
    fun provideAnnouncementRepository(api: AnnouncementApi): IAnnouncementRepository =
            AnnouncementRepositoryImpl(api)
}