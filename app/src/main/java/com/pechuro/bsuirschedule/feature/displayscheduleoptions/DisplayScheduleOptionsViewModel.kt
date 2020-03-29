package com.pechuro.bsuirschedule.feature.displayscheduleoptions

import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.common.getOrDefault
import com.pechuro.bsuirschedule.domain.entity.ScheduleDisplayType
import com.pechuro.bsuirschedule.domain.entity.SubgroupNumber
import com.pechuro.bsuirschedule.domain.interactor.GetScheduleDisplaySubgroupNumber
import com.pechuro.bsuirschedule.domain.interactor.GetScheduleDisplayType
import com.pechuro.bsuirschedule.domain.interactor.SetScheduleDisplaySubgroupNumber
import com.pechuro.bsuirschedule.domain.interactor.SetScheduleDisplayType
import com.pechuro.bsuirschedule.ext.flowLiveData
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class DisplayScheduleOptionsViewModel @Inject constructor(
        private val getScheduleDisplayType: GetScheduleDisplayType,
        private val setScheduleDisplayType: SetScheduleDisplayType,
        private val getScheduleDisplaySubgroupNumber: GetScheduleDisplaySubgroupNumber,
        private val setScheduleDisplaySubgroupNumber: SetScheduleDisplaySubgroupNumber
) : BaseViewModel() {

    val displayTypeData = flowLiveData {
        getScheduleDisplayType.execute(BaseInteractor.NoParams).getOrDefault(emptyFlow())
    }
    val subgroupNumberData = flowLiveData {
        getScheduleDisplaySubgroupNumber.execute(BaseInteractor.NoParams).getOrDefault(emptyFlow())
    }

    fun setNextDisplayType() {
        launchCoroutine {
            val currentType = displayTypeData.value ?: ScheduleDisplayType.DEFAULT
            val nextType = currentType.getNextType()
            setScheduleDisplayType.execute(SetScheduleDisplayType.Params(nextType))
        }
    }

    fun setNextSubgroupNumber() {
        launchCoroutine {
            val currentNumber = subgroupNumberData.value ?: SubgroupNumber.ALL
            val nextNumber = currentNumber.getNextNumber()
            setScheduleDisplaySubgroupNumber.execute(SetScheduleDisplaySubgroupNumber.Params(nextNumber))
        }
    }
}