package com.pechuro.bsuirschedule.feature.main.addschedule

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.common.onSuccess
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import com.pechuro.bsuirschedule.domain.interactor.GetEmployeeNames
import com.pechuro.bsuirschedule.domain.interactor.GetGroupNumbers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

class AddScheduleViewModel @Inject constructor(
        private val getGroupNumbers: GetGroupNumbers,
        private val getEmployeeNames: GetEmployeeNames
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
        state.value = State.Loading
        launchCoroutine(Dispatchers.Default) {
            delay(5000)
            withContext(Dispatchers.Main) {
                if (Random.nextBoolean()) {
                    state.value = State.Complete
                } else {
                    state.value = State.Error
                }
            }
        }
    }

    fun close() {
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