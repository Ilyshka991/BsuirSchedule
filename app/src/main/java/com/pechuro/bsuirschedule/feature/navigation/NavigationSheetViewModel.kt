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
import com.pechuro.bsuirschedule.domain.interactor.GetAvailableForUpdateSchedules
import com.pechuro.bsuirschedule.domain.interactor.GetAvailableForUpdateSchedules.Params
import com.pechuro.bsuirschedule.domain.interactor.UpdateSchedule
import com.pechuro.bsuirschedule.feature.navigation.NavigationSheetItemInformation.Content.UpdateState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NavigationSheetViewModel @Inject constructor(
        private val getAllSchedules: GetAllSchedules,
        private val deleteSchedule: DeleteSchedule,
        private val updateSchedule: UpdateSchedule,
        private val getAvailableForUpdateSchedules: GetAvailableForUpdateSchedules
) : BaseViewModel() {

    companion object {
        private const val DELAY_AFTER_UPDATE_DURATION_MS = 2000L
    }

    private val allScheduleListData = flow {
        getAllSchedules.execute(BaseInteractor.NoParams).onSuccess {
            emitAll(it)
        }
    }.asLiveData()
    private var availableForUpdateScheduleListData = flow {
        getAvailableForUpdateSchedules.execute(Params(includeAll = true)).onSuccess {
            emitAll(it)
        }
    }.asLiveData()
    private val schedulesUpdateState = MutableLiveData(emptyMap<Schedule, UpdateState>())

    val navigationInfoData = MediatorLiveData<List<NavigationSheetItemInformation>>().apply {
        addSource(allScheduleListData) { scheduleList ->
            val schedulesUpdateState = schedulesUpdateState.value ?: emptyMap()
            val availableForUpdateScheduleList = availableForUpdateScheduleListData.value
                    ?: emptyList()
            value = transformScheduleListToNavInfoList(
                    scheduleList = scheduleList,
                    availableForUpdateScheduleList = availableForUpdateScheduleList,
                    updateStates = schedulesUpdateState
            )
        }
        addSource(availableForUpdateScheduleListData) { availableForUpdateScheduleList ->
            val scheduleList = allScheduleListData.value ?: emptyList()
            val schedulesUpdateState = schedulesUpdateState.value ?: emptyMap()
            value = transformScheduleListToNavInfoList(
                    scheduleList = scheduleList,
                    availableForUpdateScheduleList = availableForUpdateScheduleList,
                    updateStates = schedulesUpdateState
            )
        }
        addSource(schedulesUpdateState) { schedulesUpdateState ->
            val scheduleList = allScheduleListData.value ?: emptyList()
            val availableForUpdateScheduleList = availableForUpdateScheduleListData.value
                    ?: emptyList()
            value = transformScheduleListToNavInfoList(
                    scheduleList = scheduleList,
                    availableForUpdateScheduleList = availableForUpdateScheduleList,
                    updateStates = schedulesUpdateState
            )
        }
    }

    fun updateSchedule(schedule: Schedule) {
        launchCoroutine {
            setUpdateState(schedule, UpdateState.IN_PROGRESS)
            val result = updateSchedule.execute(UpdateSchedule.Params(schedule))
            val resultState = if (result.isSuccess) UpdateState.SUCCESS else UpdateState.ERROR
            setUpdateState(schedule, resultState)
            delay(DELAY_AFTER_UPDATE_DURATION_MS)
            val availableForUpdateSchedules = availableForUpdateScheduleListData.value
                    ?: emptyList()
            val defaultUpdateState = if (schedule in availableForUpdateSchedules) {
                UpdateState.AVAILABLE
            } else {
                UpdateState.NOT_AVAILABLE
            }
            setUpdateState(schedule, defaultUpdateState)
        }
    }

    fun deleteSchedule(schedule: Schedule) {
        launchCoroutine {
            deleteSchedule.execute(DeleteSchedule.Params(schedule))
        }
    }

    private fun transformScheduleListToNavInfoList(
            scheduleList: List<Schedule>,
            availableForUpdateScheduleList: List<Schedule>,
            updateStates: Map<Schedule, UpdateState>
    ): List<NavigationSheetItemInformation> {
        val resultList = mutableListOf<NavigationSheetItemInformation>()

        if (scheduleList.isEmpty()) {
            resultList += NavigationSheetItemInformation.Empty
            return resultList
        }

        val allClasses = scheduleList
                .filter { it is Schedule.GroupClasses || it is Schedule.EmployeeClasses }
        if (allClasses.isNotEmpty()) {
            resultList += NavigationSheetItemInformation.Title(ScheduleType.CLASSES)
            resultList += allClasses.map {
                val updateState = when {
                    updateStates.containsKey(it) -> updateStates.getValue(it)
                    it in availableForUpdateScheduleList -> UpdateState.AVAILABLE
                    else -> UpdateState.NOT_AVAILABLE
                }
                NavigationSheetItemInformation.Content(it, updateState)
            }
        }

        val allExams = scheduleList
                .filter { it is Schedule.GroupExams || it is Schedule.EmployeeExams }
        if (allExams.isNotEmpty()) {
            resultList += NavigationSheetItemInformation.Title(ScheduleType.EXAMS)
            resultList += allExams.map {
                val updateState = when {
                    updateStates.containsKey(it) -> updateStates.getValue(it)
                    it in availableForUpdateScheduleList -> UpdateState.AVAILABLE
                    else -> UpdateState.NOT_AVAILABLE
                }
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