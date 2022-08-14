package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.entity.ScheduleItem
import com.pechuro.bsuirschedule.domain.repository.IScheduleRepository
import javax.inject.Inject

class UpdateScheduleItem @Inject constructor(
    private val scheduleRepository: IScheduleRepository
) : BaseInteractor<Unit, UpdateScheduleItem.Params>() {

    override suspend fun run(params: Params) {
        scheduleRepository.updateScheduleItem(params.scheduleItem)
    }

    data class Params(
        val scheduleItem: ScheduleItem
    )
}
