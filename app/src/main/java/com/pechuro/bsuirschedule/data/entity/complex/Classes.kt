package com.pechuro.bsuirschedule.data.entity.complex

import androidx.room.Relation
import com.pechuro.bsuirschedule.data.entity.Schedule
import com.pechuro.bsuirschedule.data.entity.ScheduleItem


class Classes(name: String, type: Int, lastUpdate: String?) :
        Schedule(name, type, lastUpdate) {
    @Relation(parentColumn = "_id", entityColumn = "schedule_id",
            entity = ScheduleItem::class)
    var schedule: List<ScheduleItem> = listOf()
}