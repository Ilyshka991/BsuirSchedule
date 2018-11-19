package com.pechuro.bsuirschedule.ui.fragment.draweroptions

import com.pechuro.bsuirschedule.ui.data.ScheduleInformation

sealed class DrawerOptionsEvent

class OnDeleted(val info: ScheduleInformation) : DrawerOptionsEvent()
class OnUpdated(val info: ScheduleInformation) : DrawerOptionsEvent()
object OnCancel : DrawerOptionsEvent()
