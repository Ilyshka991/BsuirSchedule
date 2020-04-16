package com.pechuro.bsuirschedule.feature.itemoptions

import com.pechuro.bsuirschedule.common.BaseEvent
import com.pechuro.bsuirschedule.domain.entity.ScheduleItem

data class EditScheduleItemEvent(val scheduleItems: List<ScheduleItem>) : BaseEvent()