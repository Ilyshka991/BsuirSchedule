package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import com.pechuro.bsuirschedule.domain.repository.IScheduleRepository
import javax.inject.Inject

class LoadSchedule @Inject constructor(
        private val scheduleRepository: IScheduleRepository
) : BaseInteractor<Unit, LoadSchedule.Params>() {

    override suspend fun run(params: Params) {
        scheduleRepository.getClassesList(params.name, params.types)
    }

    data class Params(
            val name: String,
            val types: List<ScheduleType>
    )
}