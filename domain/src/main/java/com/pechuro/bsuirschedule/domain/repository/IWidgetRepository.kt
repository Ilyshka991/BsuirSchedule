package com.pechuro.bsuirschedule.domain.repository

import com.pechuro.bsuirschedule.domain.entity.ScheduleWidgetInfo

interface IWidgetRepository {

    fun getScheduleWidget(widgetId: Int): ScheduleWidgetInfo?

    fun updateScheduleWidget(info: ScheduleWidgetInfo)

    fun removeScheduleWidget(widgetId: Int)
}