package com.pechuro.bsuirschedule.local.entity.schedule

import androidx.room.*
import java.util.*

@Entity(
        tableName = "group_item_exam",
        foreignKeys = [
            ForeignKey(
                    entity = GroupExamScheduleCached::class,
                    parentColumns = ["name"],
                    childColumns = ["schedule_name"],
                    onDelete = ForeignKey.CASCADE)
        ],
        indices = [Index(value = ["schedule_name"])]
)
data class GroupItemExamCached(
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