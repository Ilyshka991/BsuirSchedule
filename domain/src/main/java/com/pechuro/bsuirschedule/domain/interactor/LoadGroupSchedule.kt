package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.entity.Group
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import com.pechuro.bsuirschedule.domain.repository.IScheduleRepository
import javax.inject.Inject

class LoadGroupSchedule @Inject constructor(
    private val scheduleRepository: IScheduleRepository
) : BaseInteractor<List<Schedule>, LoadGroupSchedule.Params>() {

    override suspend fun run(params: Params): List<Schedule> {
        return scheduleRepository.loadGroupSchedule(params.group, params.types)
    }

    data class Params(
        val group: Group,
        val types: List<ScheduleType>
    )
}