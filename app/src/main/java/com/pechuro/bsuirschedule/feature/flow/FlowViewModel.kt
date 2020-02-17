package com.pechuro.bsuirschedule.feature.flow

import androidx.lifecycle.asLiveData
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.common.getOrDefault
import com.pechuro.bsuirschedule.domain.common.onSuccess
import com.pechuro.bsuirschedule.domain.interactor.CheckInfo
import com.pechuro.bsuirschedule.domain.interactor.GetAvailableForUpdateSchedules
import com.pechuro.bsuirschedule.domain.interactor.GetAvailableForUpdateSchedules.Params
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class FlowViewModel @Inject constructor(
        private val checkInfo: CheckInfo,
        private val getAvailableForUpdateSchedules: GetAvailableForUpdateSchedules
) : BaseViewModel() {

    val availableForUpdateScheduleListData = flow {
        getAvailableForUpdateSchedules.execute(Params(includeAll = false)).onSuccess {
            emitAll(it)
        }
    }.asLiveData()

    fun isInfoLoaded(): Boolean = runBlocking {
        checkInfo.execute(BaseInteractor.NoParams).getOrDefault(false)
    }
}