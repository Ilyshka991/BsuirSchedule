package com.pechuro.bsuirschedule.feature.itemoptions

import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.entity.ScheduleItem
import com.pechuro.bsuirschedule.domain.interactor.DeleteScheduleItems
import javax.inject.Inject

class ScheduleItemOptionsViewModel @Inject constructor(
    private val deleteScheduleItems: DeleteScheduleItems
) : BaseViewModel() {

    fun deleteScheduleItem(scheduleItems: List<ScheduleItem>) {
        launchCoroutine { deleteScheduleItems.execute(DeleteScheduleItems.Params(scheduleItems)) }
    }
}