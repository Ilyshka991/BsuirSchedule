package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.entity.HintDisplayState
import com.pechuro.bsuirschedule.domain.repository.ISessionRepository
import javax.inject.Inject

class SetScheduleHintDisplayState @Inject constructor(
        private val sessionRepository: ISessionRepository
) : BaseInteractor<Unit, SetScheduleHintDisplayState.Params>() {

    override suspend fun run(params: Params) {
        sessionRepository.setScheduleHintDisplayState(params.newState)
    }

    data class Params(
            val newState: HintDisplayState
    )
}