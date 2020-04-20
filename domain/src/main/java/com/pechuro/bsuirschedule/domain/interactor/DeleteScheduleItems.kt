package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.entity.ScheduleItem
import com.pechuro.bsuirschedule.domain.repository.IScheduleRepository
import javax.inject.Inject

class DeleteScheduleItems @Inject constructor(
        private val scheduleRepository: IScheduleRepository
) : BaseInteractor<Unit, DeleteScheduleItems.Params>() {

    override suspend fun run(params: Params) = scheduleRepository.deleteScheduleItems(params.scheduleItems)

    data class Params(val scheduleItems: List<ScheduleItem>)
}