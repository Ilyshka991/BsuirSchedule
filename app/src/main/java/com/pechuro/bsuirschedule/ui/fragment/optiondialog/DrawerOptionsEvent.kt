package com.pechuro.bsuirschedule.ui.fragment.optiondialog

import com.pechuro.bsuirschedule.ui.activity.navigation.transactioninfo.ScheduleInformation

sealed class DrawerOptionsEvent

class OnDelete(val info: ScheduleInformation) : DrawerOptionsEvent()
class OnUpdate(val info: ScheduleInformation) : DrawerOptionsEvent()

