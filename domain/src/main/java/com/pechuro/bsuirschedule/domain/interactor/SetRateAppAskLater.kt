package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.entity.RateAppAskInfo.Companion.ASK_LATER_DAYS_COUNT
import com.pechuro.bsuirschedule.domain.entity.getLocalDate
import com.pechuro.bsuirschedule.domain.ext.addDays
import com.pechuro.bsuirschedule.domain.repository.ISessionRepository
import java.util.Calendar
import javax.inject.Inject

class SetRateAppAskLater @Inject constructor(
    private val sessionRepository: ISessionRepository
) : BaseInteractor<Unit, BaseInteractor.NoParams>() {

    override suspend fun run(params: NoParams) {
        val previousInfo = sessionRepository.getRateAppAskInfo()
        val info = previousInfo.copy(
            askLaterDate = Calendar.getInstance().addDays(ASK_LATER_DAYS_COUNT).getLocalDate()
        )
        sessionRepository.setRateAppAskInfo(info)
    }
}