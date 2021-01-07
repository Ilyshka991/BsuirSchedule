package com.pechuro.bsuirschedule.feature.navigation

import com.pechuro.bsuirschedule.domain.entity.Schedule

sealed class NavigationSheetItemInformation(val id: Int) {

    companion object {
        const val ID_DIVIDER = 1
        const val ID_TITLE = 2
        const val ID_CONTENT = 3
        const val ID_EMPTY = 4
        const val ID_HINT = 5
    }

    object Divider : NavigationSheetItemInformation(ID_DIVIDER)

    object Empty : NavigationSheetItemInformation(ID_EMPTY)

    data class Title(val scheduleType: Type) : NavigationSheetItemInformation(ID_TITLE) {

        enum class Type {
            CLASSES, EXAMS, PART_TIME
        }
    }

    data class Content(
            val schedule: Schedule,
            val updateState: UpdateState = UpdateState.NOT_AVAILABLE,
            val isSelected: Boolean = false
    ) : NavigationSheetItemInformation(ID_CONTENT) {

        enum class UpdateState {
            NOT_AVAILABLE, AVAILABLE, IN_PROGRESS, SUCCESS, ERROR
        }
    }

    object Hint : NavigationSheetItemInformation(ID_HINT)
}