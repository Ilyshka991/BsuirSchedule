package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleItem
import com.pechuro.bsuirschedule.domain.repository.IScheduleRepository
import javax.inject.Inject

class ModifyScheduleItem @Inject constructor(
        private val scheduleRepository: IScheduleRepository
) : BaseInteractor<Unit, ModifyScheduleItem.Params>() {

    override suspend fun run(params: Params) = scheduleRepository.modifyScheduleItem(
            params.schedule,
            params.scheduleItem
    )

    data class Params(
            val schedule: Schedule,
            val scheduleItem: ScheduleItem
    )
}