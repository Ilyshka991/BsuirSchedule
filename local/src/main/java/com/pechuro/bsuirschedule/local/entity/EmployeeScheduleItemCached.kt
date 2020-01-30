package com.pechuro.bsuirschedule.local.entity

import androidx.room.*

@Entity(
        tableName = "employee_schedule_item",
        foreignKeys = [
            ForeignKey(
                    entity = EmployeeScheduleCached::class,
                    parentColumns = ["name", "type"],
                    childColumns = ["schedule_name", "schedule_type"],
                    onDelete = ForeignKey.CASCADE)
        ],
        indices = [Index(value = ["schedule_name", "schedule_type"])]
)
data class EmployeeScheduleItemCached(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        var id: Long = 0,
        @ColumnInfo(name = "schedule_name")
        val scheduleName: String,
        @ColumnInfo(name = "schedule_type")
        val scheduleType: Int,
        @ColumnInfo(name = "subject")
        val subject: String?,
        @ColumnInfo(name = "week_number")
        val weekNumbers: List<Int>?,
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
        val weekDay: String?
)