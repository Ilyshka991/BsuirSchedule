package com.pechuro.bsuirschedule.feature.appwidgetconfiguration

import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.lifecycle.MediatorLiveData
import com.pechuro.bsuirschedule.common.AppAnalytics
import com.pechuro.bsuirschedule.common.AppAnalyticsEvent
import com.pechuro.bsuirschedule.common.base.BaseViewModel
import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.common.getOrDefault
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import com.pechuro.bsuirschedule.domain.interactor.GetAllSchedules
import com.pechuro.bsuirschedule.domain.repository.IWidgetRepository
import com.pechuro.bsuirschedule.ext.flowLiveData
import com.pechuro.bsuirschedule.feature.appwidget.ScheduleWidgetDataProvider
import com.pechuro.bsuirschedule.feature.appwidget.ScheduleWidgetProvider
import com.pechuro.bsuirschedule.feature.appwidgetconfiguration.AppWidgetConfigurationScheduleDisplayData.Content
import com.pechuro.bsuirschedule.feature.appwidgetconfiguration.AppWidgetConfigurationScheduleDisplayData.Title
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class AppWidgetConfigurationViewModel @Inject constructor(
    private val widgetRepository: IWidgetRepository,
    private val context: Context,
    private val getAllSchedules: GetAllSchedules,
    private val widgetDataProvider: ScheduleWidgetDataProvider
) : BaseViewModel() {

    private val allScheduleListData = flowLiveData {
        getAllSchedules.execute(BaseInteractor.NoParams).getOrDefault(emptyFlow())
    }

    val scheduleListData by lazy(LazyThreadSafetyMode.NONE) {
        MediatorLiveData<List<AppWidgetConfigurationScheduleDisplayData>>().apply {
            addSource(allScheduleListData) {
                value = transformScheduleListToDisplayData(
                    scheduleList = it,
                    checkedSchedule = dataProvider.selectedScheduleData.value
                )
            }
            addSource(dataProvider.selectedScheduleData) {
                value = transformScheduleListToDisplayData(
                    scheduleList = allScheduleListData.value ?: emptyList(),
                    checkedSchedule = it
                )
            }
        }
    }

    lateinit var dataProvider: AppWidgetConfigurationDataProvider

    fun init(widgetId: Int) {
        if (::dataProvider.isInitialized) return
        val widgetInfo = widgetRepository.getScheduleWidget(widgetId)
        AppAnalytics.report(AppAnalyticsEvent.Widget.ConfigurationOpened(widgetInfo != null))
        dataProvider = AppWidgetConfigurationDataProvider(
            widgetId = widgetId,
            initialInfo = widgetInfo
        )
    }

    fun saveChanges() {
        val resultWidgetInfo = dataProvider.getResultWidgetInfo()
        widgetRepository.updateScheduleWidget(resultWidgetInfo)
        ScheduleWidgetProvider.updateWidget(
            context = context,
            appWidgetManager = AppWidgetManager.getInstance(context),
            widgetId = resultWidgetInfo.widgetId,
            widgetInfo = resultWidgetInfo,
            widgetData = widgetDataProvider.getScheduleItemsList(resultWidgetInfo.widgetId)
        )
        AppAnalytics.report(
            AppAnalyticsEvent.Widget.ConfigurationApplied(
                schedule = resultWidgetInfo.schedule,
                subgroupNumber = resultWidgetInfo.subgroupNumber,
                theme = resultWidgetInfo.theme
            )
        )
    }

    fun onCancelled() {
        AppAnalytics.report(AppAnalyticsEvent.Widget.ConfigurationCanceled(dataProvider.initialInfo != null))
    }

    private fun transformScheduleListToDisplayData(
        scheduleList: List<Schedule>,
        checkedSchedule: Schedule?
    ): List<AppWidgetConfigurationScheduleDisplayData> {
        if (scheduleList.isEmpty()) return emptyList()

        val resultList = mutableListOf<AppWidgetConfigurationScheduleDisplayData>()

        val allClasses = scheduleList
            .filter { it is Schedule.GroupClasses || it is Schedule.EmployeeClasses }
        if (allClasses.isNotEmpty()) {
            resultList += Title(ScheduleType.CLASSES)
            resultList += allClasses
                .sortedBy { it.name }
                .map {
                    val checked = it == checkedSchedule
                    Content(it, checked)
                }
        }

        val allExams = scheduleList
            .filter { it is Schedule.GroupExams || it is Schedule.EmployeeExams }
        if (allExams.isNotEmpty()) {
            resultList += Title(ScheduleType.EXAMS)
            resultList += allExams
                .sortedBy { it.name }
                .map {
                    val checked = it == checkedSchedule
                    Content(it, checked)
                }
        }

        return resultList.toList()
    }
}