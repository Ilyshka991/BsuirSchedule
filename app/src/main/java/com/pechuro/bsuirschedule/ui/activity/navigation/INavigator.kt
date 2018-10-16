package com.pechuro.bsuirschedule.ui.activity.navigation

import com.pechuro.bsuirschedule.ui.activity.navigation.transactioninfo.ScheduleInformation

interface INavigator {
    fun onNavigate(info: ScheduleInformation)
}