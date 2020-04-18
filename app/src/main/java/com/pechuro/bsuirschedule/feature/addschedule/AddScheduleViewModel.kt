package com.pechuro.bsuirschedule.feature.addschedule

import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.common.fold
import com.pechuro.bsuirschedule.domain.entity.Employee
import com.pechuro.bsuirschedule.domain.entity.Group
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import com.pechuro.bsuirschedule.domain.interactor.LoadEmployeeSchedule
import com.pechuro.bsuirschedule.domain.interactor.LoadGroupSchedule
import javax.inject.Inject

class AddScheduleViewModel @Inject constructor(
        private val loadEmployeeSchedule: LoadEmployeeSchedule,
        private val loadGroupSchedule: LoadGroupSchedule
) : BaseViewModel() {

    val state = MutableLiveData<State>(State.Idle)

    fun loadSchedule(group: Group, types: List<ScheduleType>) {
        if (types.isEmpty()) return
        state.value = State.Loading
        launchCoroutine {
            loadGroupSchedule.execute(LoadGroupSchedule.Params(group, types)).fold(
                    onSuccess = {
                        state.value = State.Complete(it)
                    },
                    onFailure = {
                        state.value = State.Error
                    }
            )
        }
    }

    fun loadSchedule(employee: Employee, types: List<ScheduleType>) {
        if (types.isEmpty()) return
        state.value = State.Loading
        launchCoroutine {
            loadEmployeeSchedule.execute(LoadEmployeeSchedule.Params(employee, types)).fold(
                    onSuccess = {
                        state.value = State.Complete(it)
                    },
                    onFailure = {
                        state.value = State.Error
                    }
            )
        }
    }

    fun cancel() {
        state.value = State.Idle
    }

    sealed class State {
        object Idle : State()
        object Loading : State()
        object Error : State()
        data class Complete(val schedules: List<Schedule>) : State()
    }
}