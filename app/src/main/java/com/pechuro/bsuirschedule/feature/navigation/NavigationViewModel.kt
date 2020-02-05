package com.pechuro.bsuirschedule.feature.navigation

import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.common.getOrDefault
import com.pechuro.bsuirschedule.domain.interactor.CheckInfo
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class NavigationViewModel @Inject constructor(
        private val checkInfo: CheckInfo
) : BaseViewModel() {

    fun isInfoLoaded(): Boolean = runBlocking {
        checkInfo.execute(BaseInteractor.NoParams).getOrDefault(false)
    }
}