package com.pechuro.bsuirschedule.feature.modifyitem

import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleItem
import com.pechuro.bsuirschedule.domain.interactor.AddScheduleItems
import com.pechuro.bsuirschedule.domain.interactor.DeleteScheduleItems
import javax.inject.Inject

class ModifyScheduleItemViewModel @Inject constructor(
        private val addScheduleItems: AddScheduleItems,
        private val deleteScheduleItems: DeleteScheduleItems
) : BaseViewModel() {

    lateinit var dataProvider: ModifyScheduleItemDataProvider
    val state = MutableLiveData<State>(State.Idle)

    fun init(
            schedule: Schedule,
            items: List<ScheduleItem>,
            lessonTypes: Array<String>
    ) {
        if (this::dataProvider.isInitialized) return
        dataProvider = ModifyScheduleItemDataProvider(
                lessonTypes = lessonTypes,
                initialSchedule = schedule,
                initialItems = items
        )
    }

    fun saveChanges() {
        launchCoroutine {
            state.value = State.Saving
            deleteScheduleItems.execute(DeleteScheduleItems.Params(
                    scheduleItems = dataProvider.initialItems.toList()
            ))
            addScheduleItems.execute(AddScheduleItems.Params(
                    schedule = dataProvider.initialSchedule,
                    scheduleItems = dataProvider.getResultScheduleItems()
            ))
            state.value = State.Complete
        }
    }

    fun close() {
        state.value = State.Complete
    }

    sealed class State {
        object Idle : State()
        object Saving : State()
        object Complete : State()
    }
}