package com.pechuro.bsuirschedule.feature.navigation

import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleType

sealed class NavigationSheetItemInformation(val id: Int) {

    companion object {
        const val ID_DIVIDER = 1
        const val ID_TITLE = 2
        const val ID_CONTENT = 3
        const val ID_EMPTY = 4
    }

    object Divider : NavigationSheetItemInformation(ID_DIVIDER)

    object Empty : NavigationSheetItemInformation(ID_EMPTY)

    data class Title(val scheduleType: ScheduleType) : NavigationSheetItemInformation(ID_TITLE)

    data class Content(
            val schedule: Schedule,
            var updateState: UpdateState = UpdateState.NOT_AVAILABLE
    ) : NavigationSheetItemInformation(ID_CONTENT) {

        enum class UpdateState {
            NOT_AVAILABLE, AVAILABLE, IN_PROGRESS, SUCCESS, ERROR
        }
    }
}