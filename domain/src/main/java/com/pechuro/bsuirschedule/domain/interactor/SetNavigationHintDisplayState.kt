package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.repository.ISessionRepository
import javax.inject.Inject

class SetNavigationHintDisplayState @Inject constructor(
    private val sessionRepository: ISessionRepository
) : BaseInteractor<Unit, SetNavigationHintDisplayState.Params>() {

    override suspend fun run(params: Params) {
        sessionRepository.setNavigationHintDisplayState(params.shown)
    }

    data class Params(
        val shown: Boolean
    )
}