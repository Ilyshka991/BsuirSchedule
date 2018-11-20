package com.pechuro.bsuirschedule.ui.fragment.draweroptions

import com.pechuro.bsuirschedule.ui.data.ScheduleInformation
import com.pechuro.bsuirschedule.ui.utils.BaseEvent

class OnScheduleUpdatedEvent(val info: ScheduleInformation) : BaseEvent()
class OnScheduleDeletedEvent(val info: ScheduleInformation) : BaseEvent()