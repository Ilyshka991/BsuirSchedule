package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.entity.RateAppAskInfo
import com.pechuro.bsuirschedule.domain.repository.ISessionRepository
import javax.inject.Inject

class SetRateAppInitialInfo @Inject constructor(
        private val sessionRepository: ISessionRepository
) : BaseInteractor<Unit, BaseInteractor.NoParams>() {

    override suspend fun run(params: NoParams) {
        if (!sessionRepository.isRateAppAskInfoSet()) {
            sessionRepository.setRateAppAskInfo(RateAppAskInfo())
        }
    }
}