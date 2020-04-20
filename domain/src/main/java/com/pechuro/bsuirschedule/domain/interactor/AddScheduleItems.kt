package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleItem
import com.pechuro.bsuirschedule.domain.repository.IScheduleRepository
import javax.inject.Inject

class AddScheduleItems @Inject constructor(
        private val scheduleRepository: IScheduleRepository
) : BaseInteractor<Unit, AddScheduleItems.Params>() {

    override suspend fun run(params: Params) = scheduleRepository.addScheduleItems(
            params.schedule,
            params.scheduleItems
    )

    data class Params(
            val schedule: Schedule,
            val scheduleItems: List<ScheduleItem>
    )
}