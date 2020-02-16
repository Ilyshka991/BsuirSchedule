package com.pechuro.bsuirschedule.feature.navigation

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.common.onSuccess
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import com.pechuro.bsuirschedule.domain.interactor.DeleteSchedule
import com.pechuro.bsuirschedule.domain.interactor.GetAllSchedules
import com.pechuro.bsuirschedule.domain.interactor.UpdateSchedule
import com.pechuro.bsuirschedule.feature.navigation.NavigationSheetItemInformation.Content.UpdateState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NavigationSheetViewModel @Inject constructor(
        private val getAllSchedules: GetAllSchedules,
        private val deleteSchedule: DeleteSchedule,
        private val updateSchedule: UpdateSchedule
) : BaseViewModel() {

    companion object {
        private const val DELAY_AFTER_UPDATE_DURATION_MS = 2000L
    }

    private val allScheduleListData = flow {
        getAllSchedules.execute(BaseInteractor.NoParams).onSuccess {
            emitAll(it)
        }
    }.asLiveData()
    private val schedulesUpdateState = MutableLiveData(emptyMap<Schedule, UpdateState>())

    val navigationInfoData = MediatorLiveData<List<NavigationSheetItemInformation>>().apply {
        addSource(allScheduleListData) { scheduleList ->
            value = transformScheduleListToNavInfoList(scheduleList)
        }
        addSource(schedulesUpdateState) {
            val currentScheduleList = allScheduleListData.value ?: emptyList()
            value = transformScheduleListToNavInfoList(currentScheduleList)
        }
    }

    fun updateSchedule(schedule: Schedule) {
        launchCoroutine {
            val currentSchedulesUpdateState = schedulesUpdateState.value ?: emptyMap()
            schedulesUpdateState.value = currentSchedulesUpdateState + (schedule to UpdateState.IN_PROGRESS)
            val result = updateSchedule.execute(UpdateSchedule.Params(schedule))
            val resultState = if (result.isSuccess) UpdateState.SUCCESS else UpdateState.ERROR
            setUpdateState(schedule, resultState)
            delay(DELAY_AFTER_UPDATE_DURATION_MS)
            setUpdateState(schedule, UpdateState.IDLE)
        }
    }

    fun deleteSchedule(schedule: Schedule) {
        launchCoroutine {
            deleteSchedule.execute(DeleteSchedule.Params(schedule))
        }
    }

    private fun transformScheduleListToNavInfoList(scheduleList: List<Schedule>): List<NavigationSheetItemInformation> {
        val resultList = mutableListOf<NavigationSheetItemInformation>()

        if (scheduleList.isEmpty()) {
            resultList += NavigationSheetItemInformation.Empty
            return resultList
        }

        val currentSchedulesUpdateState = schedulesUpdateState.value ?: emptyMap()

        val allClasses = scheduleList
                .filter { it is Schedule.GroupClasses || it is Schedule.EmployeeClasses }
        if (allClasses.isNotEmpty()) {
            resultList += NavigationSheetItemInformation.Title(ScheduleType.CLASSES)
            resultList += allClasses.map {
                val updateState = currentSchedulesUpdateState[it] ?: UpdateState.IDLE
                NavigationSheetItemInformation.Content(it, updateState)
            }
        }

        val allExams = scheduleList
                .filter { it is Schedule.GroupExams || it is Schedule.EmployeeExams }
        if (allExams.isNotEmpty()) {
            resultList += NavigationSheetItemInformation.Title(ScheduleType.EXAMS)
            resultList += allExams.map {
                val updateState = currentSchedulesUpdateState[it] ?: UpdateState.IDLE
                NavigationSheetItemInformation.Content(it, updateState)
            }
        }
        return resultList.toList()
    }

    private fun setUpdateState(schedule: Schedule, state: UpdateState) {
        val currentSchedulesUpdateState = schedulesUpdateState.value ?: emptyMap()
        schedulesUpdateState.value = currentSchedulesUpdateState + (schedule to state)
    }
}