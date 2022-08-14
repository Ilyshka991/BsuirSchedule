package com.pechuro.bsuirschedule.local.entity.schedule

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "employee_item_exam",
    foreignKeys = [
        ForeignKey(
            entity = EmployeeExamScheduleCached::class,
            parentColumns = ["name"],
            childColumns = ["schedule_name"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["schedule_name"])]
)
data class EmployeeItemExamCached(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0,
    @ColumnInfo(name = "schedule_name")
    val scheduleName: String,
    @ColumnInfo(name = "subject")
    val subject: String,
    @ColumnInfo(name = "subgroup_number")
    val subgroupNumber: Int,
    @ColumnInfo(name = "lesson_type")
    val lessonType: String,
    @ColumnInfo(name = "note")
    val note: String,
    @ColumnInfo(name = "start_time")
    val startTime: Date,
    @ColumnInfo(name = "end_time")
    val endTime: Date,
    @ColumnInfo(name = "week_day")
    val date: Date,
    @ColumnInfo(name = "is_added_by_user")
    val isAddedByUser: Boolean
)