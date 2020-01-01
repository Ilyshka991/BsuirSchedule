package com.pechuro.bsuirschedule.di.module

import com.pechuro.bsuirschedule.domain.interactor.CheckInfo
import com.pechuro.bsuirschedule.domain.interactor.LoadInfo
import com.pechuro.bsuirschedule.domain.repository.IEmployeeRepository
import com.pechuro.bsuirschedule.domain.repository.IGroupRepository
import dagger.Module
import dagger.Provides

@Module
class InteractorModule {

    @Provides
    fun provideCheckInfo(
            employeeRepository: IEmployeeRepository,
            groupRepository: IGroupRepository
    ): CheckInfo = CheckInfo(
            employeeRepository = employeeRepository,
            groupRepository = groupRepository
    )

    @Provides
    fun provideLoadInfo(
            employeeRepository: IEmployeeRepository,
            groupRepository: IGroupRepository
    ): LoadInfo = LoadInfo(
            employeeRepository = employeeRepository,
            groupRepository = groupRepository
    )
}