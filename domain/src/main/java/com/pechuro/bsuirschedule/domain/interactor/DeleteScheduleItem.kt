package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.entity.ScheduleItem
import com.pechuro.bsuirschedule.domain.repository.IScheduleRepository
import javax.inject.Inject

class DeleteScheduleItem @Inject constructor(
        private val scheduleRepository: IScheduleRepository
) : BaseInteractor<Unit, DeleteScheduleItem.Params>() {

    override suspend fun run(params: Params) = scheduleRepository.deleteScheduleItem(params.scheduleItem)

    data class Params(val scheduleItem: ScheduleItem)
}