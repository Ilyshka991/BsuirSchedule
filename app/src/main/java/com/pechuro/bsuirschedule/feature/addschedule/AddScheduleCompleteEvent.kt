package com.pechuro.bsuirschedule.feature.addschedule

import com.pechuro.bsuirschedule.common.BaseEvent
import com.pechuro.bsuirschedule.domain.entity.Schedule

data class AddScheduleCompleteEvent(val schedules: List<Schedule>) : BaseEvent()