package com.pechuro.bsuirschedule.feature.splash

import com.pechuro.bsuirschedule.common.BaseViewModel
import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.interactor.CheckInfo
import javax.inject.Inject

class SplashActivityViewModel @Inject constructor(
        private val checkInfo: CheckInfo
) : BaseViewModel() {

    fun isInfoLoaded(): Boolean = checkInfo.execute(BaseInteractor.NoParams).blockingGet()
}