package com.pechuro.bsuirschedule.feature

import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.interactor.SetLastOpenedSchedule
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val setLastOpenedSchedule: SetLastOpenedSchedule
) : BaseViewModel() {

    fun setLastOpenedSchedule(schedule: Schedule) {
        runBlocking {
            setLastOpenedSchedule.execute(SetLastOpenedSchedule.Params(schedule))
        }
    }
}