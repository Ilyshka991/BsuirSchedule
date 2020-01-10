package com.pechuro.bsuirschedule.feature.main.addschedule

import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

class AddScheduleViewModel @Inject constructor(

) : BaseViewModel() {

    val state = MutableLiveData<State>(State.Idle)
    val allGroupNames = MutableLiveData<List<String>>()
    val allEmployeeNames = MutableLiveData<List<String>>()

    init {
        loadSuggestions()
    }

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

    private fun loadSuggestions() {
        allGroupNames.value = listOf("1", "11")
    }

    sealed class State {
        object Idle : State()
        object Loading : State()
        object Error : State()
        object Complete : State()
    }
}