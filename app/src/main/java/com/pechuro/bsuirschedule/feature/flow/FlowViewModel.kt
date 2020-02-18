package com.pechuro.bsuirschedule.feature.flow

import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.common.getOrDefault
import com.pechuro.bsuirschedule.domain.interactor.CheckInfo
import com.pechuro.bsuirschedule.domain.interactor.GetAvailableForUpdateSchedules
import com.pechuro.bsuirschedule.domain.interactor.GetAvailableForUpdateSchedules.Params
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class FlowViewModel @Inject constructor(
        private val checkInfo: CheckInfo,
        private val getAvailableForUpdateSchedules: GetAvailableForUpdateSchedules
) : BaseViewModel() {

    val availableForUpdateScheduleListData = liveDataFlow {
        getAvailableForUpdateSchedules.execute(Params(includeAll = false)).getOrNull()
    }

    fun isInfoLoaded(): Boolean = runBlocking {
        checkInfo.execute(BaseInteractor.NoParams).getOrDefault(false)
    }
}