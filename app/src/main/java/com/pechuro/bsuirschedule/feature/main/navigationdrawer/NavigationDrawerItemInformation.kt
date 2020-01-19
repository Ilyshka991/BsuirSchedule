package com.pechuro.bsuirschedule.feature.main.navigationdrawer

import com.pechuro.bsuirschedule.domain.entity.Schedule

sealed class NavigationDrawerItemInformation(val id: Int) {

    companion object {
        const val ID_DIVIDER = 1
        const val ID_TITLE = 2
        const val ID_CONTENT = 3
        const val ID_EMPTY = 4
    }

    object Divider : NavigationDrawerItemInformation(ID_DIVIDER)

    object Empty : NavigationDrawerItemInformation(ID_EMPTY)

    data class Title(val title: String) : NavigationDrawerItemInformation(ID_TITLE)

    data class Content(val schedule: Schedule) : NavigationDrawerItemInformation(ID_CONTENT)
}