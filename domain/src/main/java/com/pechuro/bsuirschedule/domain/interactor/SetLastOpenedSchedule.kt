package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.repository.ISessionRepository
import javax.inject.Inject

class SetLastOpenedSchedule @Inject constructor(
    private val sessionRepository: ISessionRepository
) : BaseInteractor<Unit, SetLastOpenedSchedule.Params>() {

    override suspend fun run(params: Params) {
        sessionRepository.setLastOpenedSchedule(params.schedule)
    }

    data class Params(
        val schedule: Schedule?
    )
}