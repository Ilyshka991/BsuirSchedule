package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.repository.IScheduleRepository
import com.pechuro.bsuirschedule.domain.repository.IWidgetRepository
import javax.inject.Inject

class DeleteSchedule @Inject constructor(
        private val scheduleRepository: IScheduleRepository,
        private val widgetRepository: IWidgetRepository
) : BaseInteractor<Unit, DeleteSchedule.Params>() {

    override suspend fun run(params: Params) {
        widgetRepository.removeScheduleWidget(params.schedule)
        scheduleRepository.deleteSchedule(params.schedule)
    }

    data class Params(val schedule: Schedule)
}