package com.pechuro.bsuirschedule.domain.entity

data class Classes(
        val schedule: Schedule,
        val items: List<ScheduleItem>
)