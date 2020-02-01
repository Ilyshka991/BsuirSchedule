package com.pechuro.bsuirschedule.local.entity.schedule

import androidx.room.*

@Entity(
        tableName = "group_item_classes",
        foreignKeys = [
            ForeignKey(
                    entity = GroupScheduleClassesCached::class,
                    parentColumns = ["name"],
                    childColumns = ["schedule_name"],
                    onDelete = ForeignKey.CASCADE)
        ],
        indices = [Index(value = ["schedule_name"])]
)
data class GroupItemClassesCached(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        var id: Long = 0,
        @ColumnInfo(name = "schedule_name")
        val scheduleName: String,
        @ColumnInfo(name = "subject")
        val subject: String,
        @ColumnInfo(name = "week_number")
        val weekNumber: Int,
        @ColumnInfo(name = "subgroup_number")
        val subgroupNumber: Int,
        @ColumnInfo(name = "lesson_type")
        val lessonType: String,
        @ColumnInfo(name = "note")
        val note: String,
        @ColumnInfo(name = "start_time")
        val startTime: String,
        @ColumnInfo(name = "end_time")
        val endTime: String,
        @ColumnInfo(name = "week_day")
        val weekDay: Int
)