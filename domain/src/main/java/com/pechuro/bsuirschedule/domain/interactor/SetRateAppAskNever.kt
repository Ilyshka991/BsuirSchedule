package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.repository.ISessionRepository
import javax.inject.Inject

class SetRateAppAskNever @Inject constructor(
        private val sessionRepository: ISessionRepository
) : BaseInteractor<Unit, BaseInteractor.NoParams>() {

    override suspend fun run(params: NoParams) {
        val previousInfo = sessionRepository.getRateAppAskInfo()
        val info = previousInfo.copy(
                shouldAsk = false,
                askLaterDate = null
        )
        sessionRepository.setRateAppAskInfo(info)
    }
}