package com.pechuro.bsuirschedule.repository.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "all_schedules")
data class Schedule(
        @ColumnInfo(name = "schedule_name")
        var scheduleName: String,

        @ColumnInfo(name = "schedule_type")
        var scheduleType: Int,

        @ColumnInfo(name = "last_update")
        var lastUpdate: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var _id = 0
}
