package com.pechuro.bsuirschedule.feature.rateapp

import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.common.provider.AppUriProvider
import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.interactor.SetRateAppAskLater
import com.pechuro.bsuirschedule.domain.interactor.SetRateAppAskNever
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class RateAppViewModel @Inject constructor(
        val appUriProvider: AppUriProvider,
        private val setRateAppAskLater: SetRateAppAskLater,
        private val setRateAppAskNever: SetRateAppAskNever
) : BaseViewModel() {

    fun onRateAppAskLater() {
        launchCoroutine(Dispatchers.Default) {
            setRateAppAskLater.execute(BaseInteractor.NoParams)
        }
    }

    fun onRateAppAskNever() {
        launchCoroutine(Dispatchers.Default) {
            setRateAppAskNever.execute(BaseInteractor.NoParams)
        }
    }
}