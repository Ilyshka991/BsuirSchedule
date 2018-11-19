package com.pechuro.bsuirschedule.ui.fragment.drawer

import com.pechuro.bsuirschedule.ui.data.ScheduleInformation
import com.pechuro.bsuirschedule.ui.utils.BaseEvent

class OnDriwerItemLongClick(val info: ScheduleInformation) : BaseEvent()
class OnNavigate(val info: ScheduleInformation) : BaseEvent()
object OnOpenAddDialog : BaseEvent()