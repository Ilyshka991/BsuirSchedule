package com.pechuro.bsuirschedule.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
        tableName = "schedule_item",
        foreignKeys = [
            ForeignKey(
                    entity = ScheduleCached::class,
                    parentColumns = ["id"],
                    childColumns = ["schedule_id"],
                    onDelete = ForeignKey.CASCADE)
        ]
)
data class ScheduleItemCached(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        var id: Long = 0,
        @ColumnInfo(name = "schedule_id", index = true)
        val scheduleId: Long,
        @ColumnInfo(name = "subject")
        val subject: String?,
        @ColumnInfo(name = "week_number")
        val weekNumber: String?,
        @ColumnInfo(name = "subgroup_number")
        val subgroupNumber: Int?,
        @ColumnInfo(name = "lesson_type")
        val lessonType: String?,
        @ColumnInfo(name = "note")
        val note: String?,
        @ColumnInfo(name = "start_time")
        val startTime: String?,
        @ColumnInfo(name = "end_time")
        val endTime: String?,
        @ColumnInfo(name = "week_day")
        val weekDay: String
)