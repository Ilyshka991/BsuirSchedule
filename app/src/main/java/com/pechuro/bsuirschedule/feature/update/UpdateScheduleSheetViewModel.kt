package com.pechuro.bsuirschedule.feature.update

import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.common.fold
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.ext.emptyQueue
import com.pechuro.bsuirschedule.domain.interactor.SetScheduleNotRemindForUpdatesStatus
import com.pechuro.bsuirschedule.domain.interactor.SetScheduleNotRemindForUpdatesStatus.Params
import com.pechuro.bsuirschedule.domain.interactor.UpdateSchedule
import java.util.*
import javax.inject.Inject

class UpdateScheduleSheetViewModel @Inject constructor(
        private val updateSchedule: UpdateSchedule,
        private val setNotRemindStatus: SetScheduleNotRemindForUpdatesStatus
) : BaseViewModel() {

    val state = MutableLiveData<State>(State.Idle)
    val currentScheduleData = MutableLiveData<Schedule>()

    private var scheduleForUpdateQueue: Queue<Schedule> = emptyQueue()

    fun setSchedules(schedules: List<Schedule>) {
        scheduleForUpdateQueue.addAll(schedules)
        takeNextSchedule(removePrevious = false)
    }

    fun updateNextSchedule() {
        launchCoroutine {
            val scheduleForUpdate = currentScheduleData.value ?: return@launchCoroutine
            state.value = State.Loading
            updateSchedule.execute(UpdateSchedule.Params(scheduleForUpdate)).fold(
                    onSuccess = {
                        takeNextSchedule()
                    },
                    onFailure = {
                        state.value = State.Error
                    }
            )
        }
    }

    fun cancelUpdate(notRemind: Boolean) {
        launchCoroutine {
            if (notRemind) {
                val currentSchedule = currentScheduleData.value
                if (currentSchedule != null) {
                    setNotRemindStatus.execute(Params(
                            schedule = currentSchedule,
                            notRemind = notRemind
                    ))
                }
            }
            takeNextSchedule()
        }
    }

    private fun takeNextSchedule(removePrevious: Boolean = true) {
        if (removePrevious) scheduleForUpdateQueue.remove()
        val nextScheduleForUpdate = scheduleForUpdateQueue.peek()
        if (nextScheduleForUpdate == null) {
            state.value = State.Complete
        } else {
            currentScheduleData.value = nextScheduleForUpdate
            state.value = State.Idle
        }
    }

    sealed class State {
        object Idle : State()
        object Loading : State()
        object Error : State()
        object Complete : State()
    }
}