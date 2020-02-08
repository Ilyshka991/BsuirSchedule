package com.pechuro.bsuirschedule.feature.navigation

import androidx.lifecycle.asLiveData
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.common.onSuccess
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import com.pechuro.bsuirschedule.domain.interactor.GetAllSchedules
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NavigationSheetViewModel @Inject constructor(
        private val getAllSchedules: GetAllSchedules
) : BaseViewModel() {

    val schedules = flow {
        getAllSchedules.execute(BaseInteractor.NoParams).onSuccess {
            emitAll(it)
        }
    }
            .map { allScheduleList ->
                val resultList = mutableListOf<NavigationSheetItemInformation>()

                if (allScheduleList.isEmpty()) {
                    resultList += NavigationSheetItemInformation.Empty
                    return@map resultList
                }

                val allClasses = allScheduleList
                        .filter { it is Schedule.GroupClasses || it is Schedule.EmployeeClasses }
                if (allClasses.isNotEmpty()) {
                    resultList += NavigationSheetItemInformation.Title(ScheduleType.CLASSES)
                    resultList += allClasses.map { NavigationSheetItemInformation.Content(it) }
                    resultList += NavigationSheetItemInformation.Divider
                }

                val allExams = allScheduleList
                        .filter { it is Schedule.GroupExams || it is Schedule.EmployeeExams }
                if (allExams.isNotEmpty()) {
                    resultList += NavigationSheetItemInformation.Title(ScheduleType.EXAMS)
                    resultList += allExams.map { NavigationSheetItemInformation.Content(it) }
                }
                resultList.toList()
            }
            .asLiveData()
}