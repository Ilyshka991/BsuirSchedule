package com.pechuro.bsuirschedule.local.entity

data class ClassesCached(
        val schedule: ScheduleCached,
        val items: List<ScheduleItemComplex>
)