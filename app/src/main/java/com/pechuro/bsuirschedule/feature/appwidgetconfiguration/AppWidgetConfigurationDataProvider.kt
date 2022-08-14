package com.pechuro.bsuirschedule.feature.appwidgetconfiguration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleWidgetInfo
import com.pechuro.bsuirschedule.domain.entity.SubgroupNumber
import com.pechuro.bsuirschedule.ext.requireValue

class AppWidgetConfigurationDataProvider(
    val widgetId: Int,
    val initialInfo: ScheduleWidgetInfo?
) {

    private val _subgroupNumberData = MutableLiveData(SubgroupNumber.ALL)
    val subgroupNumberData: LiveData<SubgroupNumber>
        get() = _subgroupNumberData

    private val _selectedScheduleData = MutableLiveData<Schedule>()
    val selectedScheduleData: LiveData<Schedule>
        get() = _selectedScheduleData

    private val _widgetTheme = MutableLiveData(ScheduleWidgetInfo.WidgetTheme.DEFAULT)
    val widgetTheme: LiveData<ScheduleWidgetInfo.WidgetTheme>
        get() = _widgetTheme

    init {
        initialInfo?.run {
            setSubgroupNumber(subgroupNumber)
            setSelectedSchedule(schedule)
            setWidgetTheme(theme)
        }
    }

    fun setSubgroupNumber(value: SubgroupNumber) {
        _subgroupNumberData.value = value
    }

    fun setSelectedSchedule(value: Schedule) {
        _selectedScheduleData.value = value
    }

    fun setWidgetTheme(value: ScheduleWidgetInfo.WidgetTheme) {
        _widgetTheme.value = value
    }

    fun getResultWidgetInfo(): ScheduleWidgetInfo {
        return ScheduleWidgetInfo(
            widgetId = widgetId,
            schedule = selectedScheduleData.requireValue,
            subgroupNumber = subgroupNumberData.requireValue,
            theme = widgetTheme.requireValue
        )
    }
}