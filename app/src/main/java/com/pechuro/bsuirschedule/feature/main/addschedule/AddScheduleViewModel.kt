package com.pechuro.bsuirschedule.feature.main.addschedule

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.common.fold
import com.pechuro.bsuirschedule.domain.common.onSuccess
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import com.pechuro.bsuirschedule.domain.interactor.GetEmployeeNames
import com.pechuro.bsuirschedule.domain.interactor.GetGroupNumbers
import com.pechuro.bsuirschedule.domain.interactor.LoadSchedule
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddScheduleViewModel @Inject constructor(
        private val getGroupNumbers: GetGroupNumbers,
        private val getEmployeeNames: GetEmployeeNames,
        private val loadSchedule: LoadSchedule
) : BaseViewModel() {

    val state = MutableLiveData<State>(State.Idle)

    val allGroupNames = flow {
        getGroupNumbers.execute(BaseInteractor.NoParams).onSuccess {
            emitAll(it)
        }
    }.asLiveData()

    val allEmployeeNames = flow {
        getEmployeeNames.execute(BaseInteractor.NoParams).onSuccess {
            emitAll(it)
        }
    }.asLiveData()

    fun loadSchedule(name: String, types: List<ScheduleType>) {
        if (name.isEmpty() || types.isEmpty()) return
        state.value = State.Loading
        launchCoroutine {
            loadSchedule.execute(LoadSchedule.Params(name, types)).fold(
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