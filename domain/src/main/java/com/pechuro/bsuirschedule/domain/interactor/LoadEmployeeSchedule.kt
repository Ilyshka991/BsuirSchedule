package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.entity.Employee
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import com.pechuro.bsuirschedule.domain.repository.IScheduleRepository
import javax.inject.Inject

class LoadEmployeeSchedule @Inject constructor(
        private val scheduleRepository: IScheduleRepository
) : BaseInteractor<Unit, LoadEmployeeSchedule.Params>() {

    override suspend fun run(params: Params) {
        scheduleRepository.loadEmployeeSchedule(params.employee, params.types)
    }

    data class Params(
            val employee: Employee,
            val types: List<ScheduleType>
    )
}