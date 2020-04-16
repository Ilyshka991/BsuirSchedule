package com.pechuro.bsuirschedule.feature.modifyitem

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.interactor.AddScheduleItems
import com.pechuro.bsuirschedule.domain.interactor.DeleteScheduleItems
import javax.inject.Inject

class ModifyScheduleItemViewModel @Inject constructor(
        private val addScheduleItems: AddScheduleItems,
        private val deleteScheduleItems: DeleteScheduleItems,
        context: Context
) : BaseViewModel() {

    val data = ModifyScheduleItemDataProvider(context)
    val state = MutableLiveData<State>(State.Idle)

    fun saveChanges() {
        launchCoroutine {
            state.value = State.Saving
            deleteScheduleItems.execute(DeleteScheduleItems.Params(
                    scheduleItems = data.scheduleItems.toList()
            ))
            addScheduleItems.execute(AddScheduleItems.Params(
                    schedule = data.schedule,
                    scheduleItems = data.getResultScheduleItem()
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