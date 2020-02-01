package com.pechuro.bsuirschedule.feature.main.navigationdrawer

import android.content.Context
import androidx.lifecycle.asLiveData
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.common.onSuccess
import com.pechuro.bsuirschedule.domain.entity.Schedule
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

                val allClasses = allScheduleList
                        .filter { it is Schedule.GroupClasses || it is Schedule.EmployeeClasses }
                if (allClasses.isNotEmpty()) {
                    resultList += NavigationDrawerItemInformation.Title(context.getString(R.string.msg_classes))
                    resultList += allClasses.map { NavigationDrawerItemInformation.Content(it) }
                    resultList += NavigationDrawerItemInformation.Divider
                }

                val allExams = allScheduleList
                        .filter { it is Schedule.GroupExams || it is Schedule.EmployeeExams }
                if (allExams.isNotEmpty()) {
                    resultList += NavigationDrawerItemInformation.Title(context.getString(R.string.msg_exams))
                    resultList += allExams.map { NavigationDrawerItemInformation.Content(it) }
                }
                resultList
            }
            .asLiveData()
}