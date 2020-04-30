package com.pechuro.bsuirschedule.feature.lessondetails

import com.pechuro.bsuirschedule.domain.entity.*

sealed class DetailsItem {

    companion object {
        const val TIME_VIEW_TYPE = 0
        const val EMPLOYEE_INFO_VIEW_TYPE = 1
        const val WEEKS_VIEW_TYPE = 2
        const val LOCATION_HEADER_VIEW_TYPE = 3
        const val LOCATION_ITEM_VIEW_TYPE = 4
        const val LESSON_FOOTER_VIEW_TYPE = 5
    }

    open val id: Any get() = this

    val viewType: Int
        get() = when (this) {
            is Time -> TIME_VIEW_TYPE
            is EmployeeInfo -> EMPLOYEE_INFO_VIEW_TYPE
            is Weeks -> WEEKS_VIEW_TYPE
            LocationHeader -> LOCATION_HEADER_VIEW_TYPE
            is LocationItem -> LOCATION_ITEM_VIEW_TYPE
            is LessonFooter -> LESSON_FOOTER_VIEW_TYPE
        }

    data class Time(
            val startTime: LocalTime,
            val endTime: LocalTime
    ) : DetailsItem()

    data class EmployeeInfo(val employees: List<Employee>) : DetailsItem()

    data class Weeks(val weeks: List<WeekNumber>) : DetailsItem()

    object LocationHeader : DetailsItem()

    data class LocationItem(val auditory: Auditory) : DetailsItem() {
        override val id: Any get() = auditory.id
    }

    data class LessonFooter(
            val note: String,
            val onNoteChanged: (note: String) -> Unit
    ) : DetailsItem()
}
