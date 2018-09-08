package com.pechuro.bsuirschedule.repository.entity.item

import android.arch.persistence.room.*
import com.pechuro.bsuirschedule.repository.entity.Schedule

@Entity(tableName = "student_class")
data class StudentClassItem(
        @ColumnInfo(name = "subject")
        val subject: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id = 0

    @ForeignKey(entity = Schedule::class,
            parentColumns = ["_id"], childColumns = ["schedule_id"],
            onDelete = ForeignKey.CASCADE)
    @ColumnInfo(name = "schedule_id")
    var scheduleId: Int = 0
}