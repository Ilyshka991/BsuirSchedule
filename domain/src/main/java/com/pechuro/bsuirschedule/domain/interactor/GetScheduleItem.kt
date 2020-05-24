package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleItem
import com.pechuro.bsuirschedule.domain.repository.IScheduleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetScheduleItem @Inject constructor(
        private val scheduleRepository: IScheduleRepository
) : BaseInteractor<Flow<ScheduleItem>, GetScheduleItem.Params>() {

    override suspend fun run(params: Params) = scheduleRepository.getScheduleItem(
            schedule = params.schedule,
            itemId = params.itemId
    )

    data class Params(
            val schedule: Schedule,
            val itemId: Long
    )
}