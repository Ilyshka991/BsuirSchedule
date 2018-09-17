package com.pechuro.bsuirschedule.data.entity

import android.arch.persistence.room.*
import com.google.gson.annotations.SerializedName
import com.pechuro.bsuirschedule.data.database.Converters

@Entity(tableName = "schedule_item")
@TypeConverters(Converters::class)
data class ScheduleItem(
        val subject: String?,
        val weekNumber: List<Int>?,
        @SerializedName("numSubgroup")
        val subgroupNumber: Int?,
        val lessonType: String?,
        val auditory: List<String>?,
        val note: String?,
        @SerializedName("startLessonTime")
        val startTime: String?,
        @SerializedName("endLessonTime")
        val endTime: String?,
        val employee: List<Employee>?) {
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

    @Transient
    @ColumnInfo(name = "week_day")
    lateinit var weekDay: String
}