package com.pechuro.bsuirschedule.di.module

import com.pechuro.bsuirschedule.domain.interactor.CheckInfo
import com.pechuro.bsuirschedule.domain.interactor.LoadInfo
import com.pechuro.bsuirschedule.domain.repository.IBuildingRepository
import com.pechuro.bsuirschedule.domain.repository.IEmployeeRepository
import com.pechuro.bsuirschedule.domain.repository.IGroupRepository
import com.pechuro.bsuirschedule.domain.repository.ISpecialityRepository
import dagger.Module
import dagger.Provides

@Module
class InteractorModule {

    @Provides
    fun provideCheckInfo(
            employeeRepository: IEmployeeRepository,
            groupRepository: IGroupRepository,
            specialityRepository: ISpecialityRepository,
            buildingRepository: IBuildingRepository
    ): CheckInfo = CheckInfo(
            employeeRepository = employeeRepository,
            groupRepository = groupRepository,
            specialityRepository = specialityRepository,
            buildingRepository = buildingRepository
    )

    @Provides
    fun provideLoadInfo(
            employeeRepository: IEmployeeRepository,
            groupRepository: IGroupRepository,
            specialityRepository: ISpecialityRepository,
            buildingRepository: IBuildingRepository
    ): LoadInfo = LoadInfo(
            employeeRepository = employeeRepository,
            groupRepository = groupRepository,
            specialityRepository = specialityRepository,
            buildingRepository = buildingRepository
    )
}