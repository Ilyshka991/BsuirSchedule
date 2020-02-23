package com.pechuro.bsuirschedule.feature.navigation

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.common.getOrDefault
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import com.pechuro.bsuirschedule.domain.interactor.*
import com.pechuro.bsuirschedule.domain.interactor.GetAvailableForUpdateSchedules.Params
import com.pechuro.bsuirschedule.ext.flowLiveData
import com.pechuro.bsuirschedule.feature.navigation.NavigationSheetItemInformation.Content.UpdateState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class NavigationSheetViewModel @Inject constructor(
        private val getAllSchedules: GetAllSchedules,
        private val deleteSchedule: DeleteSchedule,
        private val updateSchedule: UpdateSchedule,
        private val getAvailableForUpdateSchedules: GetAvailableForUpdateSchedules,
        private val getLastOpenedSchedule: GetLastOpenedSchedule
) : BaseViewModel() {

    companion object {
        private const val DELAY_AFTER_UPDATE_DURATION_MS = 2000L
    }

    private val allScheduleListData = flowLiveData {
        getAllSchedules.execute(BaseInteractor.NoParams).getOrDefault(emptyFlow())
    }
    private val availableForUpdateScheduleListData = flowLiveData {
        getAvailableForUpdateSchedules.execute(Params(includeAll = true)).getOrDefault(emptyFlow())
    }
    private val selectedScheduleData = flowLiveData {
        getLastOpenedSchedule.execute(BaseInteractor.NoParams).getOrDefault(emptyFlow())
    }
    private val schedulesUpdateState = MutableLiveData(emptyMap<Schedule, UpdateState>())

    val navigationInfoData = MediatorLiveData<List<NavigationSheetItemInformation>>().apply {
        addSource(allScheduleListData) { updateNavigationInfo() }
        addSource(availableForUpdateScheduleListData) { updateNavigationInfo() }
        addSource(schedulesUpdateState) { updateNavigationInfo() }
        addSource(selectedScheduleData) { updateNavigationInfo() }
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

    private fun updateNavigationInfo() {
        launchCoroutine {
            val scheduleList = allScheduleListData.value ?: emptyList()
            val availableForUpdateScheduleList = availableForUpdateScheduleListData.value
                    ?: emptyList()
            val schedulesUpdateState = schedulesUpdateState.value ?: emptyMap()
            val selectedSchedule = selectedScheduleData.value
            navigationInfoData.value = transformScheduleListToNavInfoList(
                    scheduleList = scheduleList,
                    availableForUpdateScheduleList = availableForUpdateScheduleList,
                    updateStates = schedulesUpdateState,
                    selectedSchedule = selectedSchedule
            )
        }
    }

    private fun transformScheduleListToNavInfoList(
            scheduleList: List<Schedule>,
            availableForUpdateScheduleList: List<Schedule>,
            updateStates: Map<Schedule, UpdateState>,
            selectedSchedule: Schedule?
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
                val isSelected = it == selectedSchedule
                NavigationSheetItemInformation.Content(it, updateState, isSelected)
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
                val isSelected = it == selectedSchedule
                NavigationSheetItemInformation.Content(it, updateState, isSelected)
            }
        }
        return resultList.toList()
    }

    private fun setUpdateState(schedule: Schedule, state: UpdateState) {
        val currentSchedulesUpdateState = schedulesUpdateState.value ?: emptyMap()
        schedulesUpdateState.value = currentSchedulesUpdateState + (schedule to state)
    }
}