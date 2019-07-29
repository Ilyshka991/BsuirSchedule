package com.pechuro.bsuirschedule.local.database.entity.complex

import com.pechuro.bsuirschedule.local.database.entity.Schedule
import com.pechuro.bsuirschedule.local.database.entity.ScheduleItem

data class Classes(
        val schedule: Schedule,
        val items: List<ScheduleItem>
)