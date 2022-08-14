package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.entity.LocalDate
import com.pechuro.bsuirschedule.domain.entity.RateAppAskInfo.Companion.REQUIRED_DAYS_LEFT
import com.pechuro.bsuirschedule.domain.entity.toDate
import com.pechuro.bsuirschedule.domain.repository.ISessionRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ShouldAskRateApp @Inject constructor(
    private val sessionRepository: ISessionRepository
) : BaseInteractor<Boolean, BaseInteractor.NoParams>() {

    override suspend fun run(params: NoParams): Boolean {
        val lastInfo = sessionRepository.getRateAppAskInfo()
        val askLaterDate = lastInfo.askLaterDate
        return when {
            !lastInfo.shouldAsk -> false
            askLaterDate != null -> askLaterDate <= LocalDate.current()
            else -> {
                val diff = LocalDate.current().toDate().time - lastInfo.installDate.toDate().time
                val daysLeft = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
                daysLeft >= REQUIRED_DAYS_LEFT
            }
        }
    }
}