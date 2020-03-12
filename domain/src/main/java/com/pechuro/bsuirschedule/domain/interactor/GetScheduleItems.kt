package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleItem
import com.pechuro.bsuirschedule.domain.repository.IScheduleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetScheduleItems @Inject constructor(
        private val scheduleRepository: IScheduleRepository
) : BaseInteractor<Flow<List<ScheduleItem>>, GetScheduleItems.Params>() {

    override suspend fun run(params: Params) = scheduleRepository.getScheduleItems(params.schedule)

    data class Params(
            val schedule: Schedule
    )
}