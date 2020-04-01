package com.pechuro.bsuirschedule.feature.scheduleitemoptions

import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.entity.ScheduleItem
import com.pechuro.bsuirschedule.domain.interactor.DeleteScheduleItem
import javax.inject.Inject

class ScheduleItemOptionsViewModel @Inject constructor(
        private val deleteScheduleItem: DeleteScheduleItem
) : BaseViewModel() {

    fun deleteScheduleItem(scheduleItem: ScheduleItem) {
        launchCoroutine { deleteScheduleItem.execute(DeleteScheduleItem.Params(scheduleItem)) }
    }
}