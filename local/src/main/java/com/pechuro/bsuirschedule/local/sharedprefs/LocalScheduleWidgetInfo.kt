package com.pechuro.bsuirschedule.local.sharedprefs

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocalScheduleWidgetInfo(
    val widgetId: Int,
    val schedule: LocalScheduleInfo,
    val subgroupNumber: Int,
    val theme: String
)


