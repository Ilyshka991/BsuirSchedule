package com.pechuro.bsuirschedule.feature.main.addschedule

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.common.fold
import com.pechuro.bsuirschedule.domain.common.onSuccess
import com.pechuro.bsuirschedule.domain.entity.Employee
import com.pechuro.bsuirschedule.domain.entity.Group
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import com.pechuro.bsuirschedule.domain.interactor.*
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddScheduleViewModel @Inject constructor(
        private val getGroups: GetGroups,
        private val getEmployees: GetEmployees,
        private val loadEmployeeSchedule: LoadEmployeeSchedule,
        private val loadGroupSchedule: LoadGroupSchedule,
        private val tmp: LoadAllSchedules
) : BaseViewModel() {

    val state = MutableLiveData<State>(State.Idle)

    val allGroupNames = flow {
        getGroups.execute(BaseInteractor.NoParams).onSuccess {
            emitAll(it)
        }
    }.asLiveData()

    val allEmployeeNames = flow {
        getEmployees.execute(BaseInteractor.NoParams).onSuccess {
            emitAll(it)
        }
    }.asLiveData()

    init {
        launchCoroutine {
            tmp.execute(BaseInteractor.NoParams)
        }
    }

    fun loadSchedule(group: Group, types: List<ScheduleType>) {
        if (types.isEmpty()) return
        state.value = State.Loading
        launchCoroutine {
            loadGroupSchedule.execute(LoadGroupSchedule.Params(group, types)).fold(
                    onSuccess = {
                        state.value = State.Complete
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
                        state.value = State.Complete
                    },
                    onFailure = {
                        state.value = State.Error
                    }
            )
        }
    }

    fun complete() {
        state.value = State.Complete
    }

    fun cancel() {
        state.value = State.Idle
    }

    sealed class State {
        object Idle : State()
        object Loading : State()
        object Error : State()
        object Complete : State()
    }
}