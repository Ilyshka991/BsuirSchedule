package com.pechuro.bsuirschedule.feature.navigation

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.common.AppAnalytics
import com.pechuro.bsuirschedule.common.AppAnalyticsEvent
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.common.fold
import com.pechuro.bsuirschedule.domain.common.getOrDefault
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.isPartTime
import com.pechuro.bsuirschedule.domain.interactor.DeleteSchedule
import com.pechuro.bsuirschedule.domain.interactor.GetAllSchedules
import com.pechuro.bsuirschedule.domain.interactor.GetAvailableForUpdateSchedules
import com.pechuro.bsuirschedule.domain.interactor.GetAvailableForUpdateSchedules.Params
import com.pechuro.bsuirschedule.domain.interactor.GetLastOpenedSchedule
import com.pechuro.bsuirschedule.domain.interactor.GetNavigationHintDisplayState
import com.pechuro.bsuirschedule.domain.interactor.SetNavigationHintDisplayState
import com.pechuro.bsuirschedule.domain.interactor.UpdateSchedule
import com.pechuro.bsuirschedule.ext.flowLiveData
import com.pechuro.bsuirschedule.feature.navigation.NavigationSheetItemInformation.Content.UpdateState
import com.pechuro.bsuirschedule.feature.navigation.NavigationSheetItemInformation.Title
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NavigationSheetViewModel @Inject constructor(
    private val getAllSchedules: GetAllSchedules,
    private val deleteSchedule: DeleteSchedule,
    private val updateSchedule: UpdateSchedule,
    private val getAvailableForUpdateSchedules: GetAvailableForUpdateSchedules,
    private val getLastOpenedSchedule: GetLastOpenedSchedule,
    private val getNavigationHintDisplayState: GetNavigationHintDisplayState,
    private val setNavigationHintDisplayState: SetNavigationHintDisplayState
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
    private val hintDisplayState = flowLiveData {
        getNavigationHintDisplayState.execute(BaseInteractor.NoParams).getOrDefault(emptyFlow())
    }
    private val schedulesUpdateState = MutableLiveData(emptyMap<Schedule, UpdateState>())

    val navigationInfoData = MediatorLiveData<List<NavigationSheetItemInformation>>().apply {
        addSource(allScheduleListData) { updateNavigationInfo() }
        addSource(availableForUpdateScheduleListData) { updateNavigationInfo() }
        addSource(schedulesUpdateState) { updateNavigationInfo() }
        addSource(selectedScheduleData) { updateNavigationInfo() }
        addSource(hintDisplayState) { updateNavigationInfo() }
    }

    private var updateInfoJob: Job? = null

    fun updateSchedule(schedule: Schedule) {
        launchCoroutine {
            setUpdateState(schedule, UpdateState.IN_PROGRESS)
            val result = updateSchedule.execute(UpdateSchedule.Params(schedule))
            val resultState = result.fold(
                onSuccess = {
                    AppAnalytics.report(AppAnalyticsEvent.Navigation.ScheduleUpdateSuccess(schedule))
                    UpdateState.SUCCESS
                },
                onFailure = {
                    AppAnalytics.report(AppAnalyticsEvent.Navigation.ScheduleUpdateFail(it))
                    UpdateState.ERROR
                }
            )
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

    fun onHintDismissed() {
        launchCoroutine {
            setNavigationHintDisplayState.execute(SetNavigationHintDisplayState.Params(shown = true))
        }
    }

    private fun updateNavigationInfo() {
        updateInfoJob?.cancel()
        updateInfoJob = launchCoroutine {
            val scheduleList = allScheduleListData.value ?: emptyList()
            val availableForUpdateScheduleList = availableForUpdateScheduleListData.value
                ?: emptyList()
            val schedulesUpdateState = schedulesUpdateState.value ?: emptyMap()
            val selectedSchedule = selectedScheduleData.value
            val hintShown = hintDisplayState.value ?: false
            val newInfo = withContext(Dispatchers.IO) {
                transformScheduleListToNavInfoList(
                    scheduleList = scheduleList,
                    availableForUpdateScheduleList = availableForUpdateScheduleList,
                    updateStates = schedulesUpdateState,
                    selectedSchedule = selectedSchedule,
                    hintShown = hintShown
                )
            }
            navigationInfoData.value = newInfo
        }
    }

    private fun transformScheduleListToNavInfoList(
        scheduleList: List<Schedule>,
        availableForUpdateScheduleList: List<Schedule>,
        updateStates: Map<Schedule, UpdateState>,
        selectedSchedule: Schedule?,
        hintShown: Boolean
    ): List<NavigationSheetItemInformation> {
        val resultList = mutableListOf<NavigationSheetItemInformation>()

        if (!hintShown && scheduleList.isNotEmpty()) {
            resultList += NavigationSheetItemInformation.Hint
        }
        if (scheduleList.isEmpty()) {
            resultList += NavigationSheetItemInformation.Empty
            return resultList
        }

        val allClasses = scheduleList.filter {
            it is Schedule.GroupClasses || it is Schedule.EmployeeClasses
        }
        if (allClasses.isNotEmpty()) {
            resultList += Title(Title.Type.CLASSES)
            resultList += allClasses.mapToNavInfo(
                availableForUpdateScheduleList = availableForUpdateScheduleList,
                updateStates = updateStates,
                selectedSchedule = selectedSchedule
            )
        }

        val allExams = scheduleList.filter {
            (it is Schedule.GroupExams && !it.group.speciality.educationForm.isPartTime)
                    || it is Schedule.EmployeeExams
        }
        if (allExams.isNotEmpty()) {
            resultList += Title(Title.Type.EXAMS)
            resultList += allExams.mapToNavInfo(
                availableForUpdateScheduleList = availableForUpdateScheduleList,
                updateStates = updateStates,
                selectedSchedule = selectedSchedule
            )
        }

        val allPartTime = scheduleList.filter {
            it is Schedule.GroupExams && it.group.speciality.educationForm.isPartTime
        }
        if (allPartTime.isNotEmpty()) {
            resultList += Title(Title.Type.PART_TIME)
            resultList += allPartTime.mapToNavInfo(
                availableForUpdateScheduleList = availableForUpdateScheduleList,
                updateStates = updateStates,
                selectedSchedule = selectedSchedule
            )
        }

        return resultList.toList()
    }

    private fun List<Schedule>.mapToNavInfo(
        availableForUpdateScheduleList: List<Schedule>,
        updateStates: Map<Schedule, UpdateState>,
        selectedSchedule: Schedule?
    ) = sortedBy { it.name }.map {
        val updateState = when {
            updateStates.containsKey(it) -> updateStates.getValue(it)
            it in availableForUpdateScheduleList -> UpdateState.AVAILABLE
            else -> UpdateState.NOT_AVAILABLE
        }
        val isSelected = it == selectedSchedule
        NavigationSheetItemInformation.Content(it, updateState, isSelected)
    }

    private fun setUpdateState(schedule: Schedule, state: UpdateState) {
        val currentSchedulesUpdateState = schedulesUpdateState.value ?: emptyMap()
        schedulesUpdateState.value = currentSchedulesUpdateState + (schedule to state)
    }
}