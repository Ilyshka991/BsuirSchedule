package com.pechuro.bsuirschedule.feature.main.navigationdrawer

import android.content.Context
import androidx.lifecycle.asLiveData
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.common.onSuccess
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import com.pechuro.bsuirschedule.domain.interactor.GetAllSchedules
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NavigationDrawerFragmentViewModel @Inject constructor(
        private val getAllSchedules: GetAllSchedules,
        private val context: Context
) : BaseViewModel() {

    val schedules = flow {
        getAllSchedules.execute(BaseInteractor.NoParams).onSuccess {
            emitAll(it)
        }
    }
            .map { allScheduleList ->
                val resultList = mutableListOf<NavigationDrawerItemInformation>()

                if (allScheduleList.isEmpty()) {
                    resultList += NavigationDrawerItemInformation.Empty
                    return@map resultList
                }

                val studentClasses = allScheduleList.filter { it.type == ScheduleType.STUDENT_CLASSES }
                val employeeClasses = allScheduleList.filter { it.type == ScheduleType.EMPLOYEE_CLASSES }
                if (studentClasses.isNotEmpty() || employeeClasses.isNotEmpty()) {
                    resultList += NavigationDrawerItemInformation.Title(context.getString(R.string.msg_classes))
                    resultList += studentClasses.map { NavigationDrawerItemInformation.Content(it) }
                    resultList += employeeClasses.map { NavigationDrawerItemInformation.Content(it) }
                    resultList += NavigationDrawerItemInformation.Divider
                }

                val studentExams = allScheduleList.filter { it.type == ScheduleType.STUDENT_EXAMS }
                val employeeExams = allScheduleList.filter { it.type == ScheduleType.EMPLOYEE_EXAMS }
                if (studentExams.isNotEmpty() || employeeExams.isNotEmpty()) {
                    resultList += NavigationDrawerItemInformation.Title(context.getString(R.string.msg_exams))
                    resultList += studentExams.map { NavigationDrawerItemInformation.Content(it) }
                    resultList += employeeExams.map { NavigationDrawerItemInformation.Content(it) }
                }
                resultList
            }
            .asLiveData()
}