package com.pechuro.bsuirschedule.repository.entity.complex

import android.arch.persistence.room.Relation
import com.pechuro.bsuirschedule.repository.entity.Schedule
import com.pechuro.bsuirschedule.repository.entity.ScheduleItem


class Classes(name: String, type: Int, lastUpdate: String?) :
        Schedule(name, type, lastUpdate) {
    @Relation(parentColumn = "_id", entityColumn = "schedule_id",
            entity = ScheduleItem::class)
    var schedule: List<ScheduleItem> = listOf()
}