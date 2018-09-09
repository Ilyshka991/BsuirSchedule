package com.pechuro.bsuirschedule.repository.entity

import android.arch.persistence.room.Relation


class Classes(name: String, type: Int, lastUpdate: String) :
        Schedule(name, type, lastUpdate) {
    @Relation(parentColumn = "_id", entityColumn = "schedule_id",
            entity = ScheduleItem::class)
    var classes: List<ScheduleItem> = listOf()
}