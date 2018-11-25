package com.pechuro.bsuirschedule.data.entity

import androidx.room.*
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.pechuro.bsuirschedule.data.utils.Converters
import com.pechuro.bsuirschedule.data.utils.WeekNumberTypeAdapter

@Entity(tableName = "schedule_item", foreignKeys = [ForeignKey(entity = Schedule::class,
        parentColumns = ["_id"], childColumns = ["schedule_id"],
        onDelete = ForeignKey.CASCADE)])
@TypeConverters(Converters::class)
data class ScheduleItem(
        val subject: String?,
        @JsonAdapter(WeekNumberTypeAdapter::class)
        val weekNumber: String?,
        @SerializedName("numSubgroup")
        val subgroupNumber: Int?,
        val lessonType: String?,
        @SerializedName("auditory")
        val auditories: MutableList<String>?,
        val note: String?,
        @SerializedName("startLessonTime")
        val startTime: String?,
        @SerializedName("endLessonTime")
        val endTime: String?,
        @SerializedName("employee")
        val employees: List<Employee>?) {
    @Transient
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id")
    var id = 0

    @Transient
    @ColumnInfo(name = "schedule_id")
    var scheduleId: Int = 0

    @Transient
    @ColumnInfo(name = "week_day")
    lateinit var weekDay: String
}