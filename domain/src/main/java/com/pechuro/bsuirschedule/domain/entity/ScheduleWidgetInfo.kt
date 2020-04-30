package com.pechuro.bsuirschedule.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ScheduleWidgetInfo(
        val widgetId: Int,
        val schedule: Schedule,
        val subgroupNumber: SubgroupNumber,
        val theme: WidgetTheme
) : Parcelable {

    enum class WidgetTheme {
        SYSTEM, LIGHT, DARK;

        companion object {
            val DEFAULT = SYSTEM

            fun getForName(name: String) = valueOf(name)
        }
    }
}

