package com.pechuro.bsuirschedule.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "schedule_item", foreignKeys = [ForeignKey(entity = Schedule::class,
        parentColumns = ["_id"], childColumns = ["schedule_id"],
        onDelete = ForeignKey.CASCADE)])
data class ScheduleItem(
        @ColumnInfo(name = "subject")
        val subject: String?,
        @ColumnInfo(name = "week_number")
        val weekNumber: String?,
        @ColumnInfo(name = "subgroup_number")
        val subgroupNumber: Int?,
        @ColumnInfo(name = "lesson_type")
        val lessonType: String?,
        /*val auditories: List<String>?,*/
        @ColumnInfo(name = "note")
        val note: String?,
        @ColumnInfo(name = "start_time")
        val startTime: String?,
        @ColumnInfo(name = "end_time")
        val endTime: String?,
        @ColumnInfo(name = "week_day")
        val weekDay: String,
        @ColumnInfo(name = "schedule_id")
        var scheduleId: Long = 0
        /*val employees: List<Employee>?*/) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id")
    var id = 0
}