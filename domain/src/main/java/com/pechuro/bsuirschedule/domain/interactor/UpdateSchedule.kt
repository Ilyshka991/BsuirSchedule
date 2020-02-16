package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.repository.IScheduleRepository
import javax.inject.Inject

class UpdateSchedule @Inject constructor(
        private val scheduleRepository: IScheduleRepository
) : BaseInteractor<Unit, UpdateSchedule.Params>() {

    override suspend fun run(params: Params) = scheduleRepository.update(params.schedule)

    data class Params(val schedule: Schedule)
}