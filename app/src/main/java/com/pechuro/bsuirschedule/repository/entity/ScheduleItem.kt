package com.pechuro.bsuirschedule.repository.entity

import android.arch.persistence.room.*
import com.pechuro.bsuirschedule.repository.utils.Converters

@Entity(tableName = "schedule_item")
@TypeConverters(Converters::class)
data class ScheduleItem(
        val subject: String,
        val weekNumber: List<Int>
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

    @Transient
    @ColumnInfo(name = "week_day")
    lateinit var weekDay: String
}