package com.pechuro.bsuirschedule.feature.scheduleitemoptions

import com.pechuro.bsuirschedule.common.BaseEvent
import com.pechuro.bsuirschedule.domain.entity.ScheduleItem

data class EditScheduleItemEvent(val scheduleItem: ScheduleItem) : BaseEvent()