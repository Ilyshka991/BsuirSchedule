package com.pechuro.bsuirschedule.repository.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "schedule_item")
data class ScheduleItem(
        @ColumnInfo(name = "subject")
        val subject: String
) {
    @Transient
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id = 0

    @Transient
    @ForeignKey(entity = Schedule::class,
            parentColumns = ["_id"], childColumns = ["schedule_id"],
            onDelete = ForeignKey.CASCADE)
    @ColumnInfo(name = "schedule_id")
    var scheduleId: Int = 0
}