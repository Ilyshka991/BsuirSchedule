package com.pechuro.bsuirschedule.di.module

import com.pechuro.bsuirschedule.data.repository.BuildingRepositoryImpl
import com.pechuro.bsuirschedule.data.repository.EmployeeRepositoryImpl
import com.pechuro.bsuirschedule.data.repository.GroupRepositoryImpl
import com.pechuro.bsuirschedule.data.repository.ScheduleRepositoryImpl
import com.pechuro.bsuirschedule.data.repository.SessionRepositoryImpl
import com.pechuro.bsuirschedule.data.repository.SpecialityRepositoryImpl
import com.pechuro.bsuirschedule.data.repository.WidgetRepositoryImpl
import com.pechuro.bsuirschedule.di.annotations.AppScope
import com.pechuro.bsuirschedule.domain.repository.IBuildingRepository
import com.pechuro.bsuirschedule.domain.repository.IEmployeeRepository
import com.pechuro.bsuirschedule.domain.repository.IGroupRepository
import com.pechuro.bsuirschedule.domain.repository.IScheduleRepository
import com.pechuro.bsuirschedule.domain.repository.ISessionRepository
import com.pechuro.bsuirschedule.domain.repository.ISpecialityRepository
import com.pechuro.bsuirschedule.domain.repository.IWidgetRepository
import com.pechuro.bsuirschedule.local.dao.BuildingDao
import com.pechuro.bsuirschedule.local.dao.EmployeeDao
import com.pechuro.bsuirschedule.local.dao.GroupDao
import com.pechuro.bsuirschedule.local.dao.ScheduleDao
import com.pechuro.bsuirschedule.local.dao.SpecialityDao
import com.pechuro.bsuirschedule.local.sharedprefs.SharedPreferencesManager
import com.pechuro.bsuirschedule.remote.api.BuildingApi
import com.pechuro.bsuirschedule.remote.api.ScheduleApi
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
        specialityRepository: ISpecialityRepository,
        employeeRepository: IEmployeeRepository
    ): IScheduleRepository = ScheduleRepositoryImpl(
        api = api,
        dao = dao,
        groupRepository = groupRepository,
        specialityRepository = specialityRepository,
        employeeRepository = employeeRepository,
        buildingRepository = buildingRepository
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
    fun provideSessionRepository(
        sharedPreferencesManager: SharedPreferencesManager,
        scheduleRepository: IScheduleRepository
    ): ISessionRepository = SessionRepositoryImpl(
        sharedPreferencesManager = sharedPreferencesManager,
        scheduleRepository = scheduleRepository
    )

    @Provides
    @AppScope
    fun provideWidgetRepository(
        sharedPreferencesManager: SharedPreferencesManager,
        scheduleRepository: IScheduleRepository
    ): IWidgetRepository = WidgetRepositoryImpl(
        sharedPreferencesManager = sharedPreferencesManager,
        scheduleRepository = scheduleRepository
    )
}