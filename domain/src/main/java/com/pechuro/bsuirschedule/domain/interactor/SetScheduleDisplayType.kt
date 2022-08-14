package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.entity.ScheduleDisplayType
import com.pechuro.bsuirschedule.domain.repository.ISessionRepository
import javax.inject.Inject

class SetScheduleDisplayType @Inject constructor(
    private val sessionRepository: ISessionRepository
) : BaseInteractor<Unit, SetScheduleDisplayType.Params>() {

    override suspend fun run(params: Params) {
        sessionRepository.setScheduleDisplayType(params.type)
    }

    data class Params(
        val type: ScheduleDisplayType
    )
}