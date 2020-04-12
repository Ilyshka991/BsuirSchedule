package com.pechuro.bsuirschedule.feature.modifyitem

import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleItem
import com.pechuro.bsuirschedule.domain.interactor.ModifyScheduleItem
import javax.inject.Inject

class ModifyScheduleItemViewModel @Inject constructor(
        private val modifyScheduleItem: ModifyScheduleItem
) : BaseViewModel() {

    val state = MutableLiveData<State>(State.Idle)

    fun saveChanges(schedule: Schedule, scheduleItem: ScheduleItem) {
        launchCoroutine {
            state.value = State.Saving
            modifyScheduleItem.execute(ModifyScheduleItem.Params(
                    schedule = schedule,
                    scheduleItem = scheduleItem
            ))
            state.value = State.Complete
        }
    }

    sealed class State {
        object Idle : State()
        object Saving : State()
        object Complete : State()
    }
}