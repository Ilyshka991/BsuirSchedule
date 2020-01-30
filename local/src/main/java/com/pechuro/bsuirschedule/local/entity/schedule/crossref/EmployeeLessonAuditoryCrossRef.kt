package com.pechuro.bsuirschedule.local.entity.schedule.crossref

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.pechuro.bsuirschedule.local.entity.building.AuditoryCached
import com.pechuro.bsuirschedule.local.entity.schedule.EmployeeLessonCached

@Entity(
        tableName = "employee_lesson_auditory_join",
        primaryKeys = ["schedule_item_id", "auditory_id"],
        foreignKeys = [
            ForeignKey(
                    entity = EmployeeLessonCached::class,
                    parentColumns = ["id"],
                    childColumns = ["schedule_item_id"],
                    onDelete = ForeignKey.CASCADE),
            ForeignKey(
                    entity = AuditoryCached::class,
                    parentColumns = ["id"],
                    childColumns = ["auditory_id"],
                    onDelete = ForeignKey.NO_ACTION)
        ]
)
data class EmployeeLessonAuditoryCrossRef(
        @ColumnInfo(name = "schedule_item_id", index = true)
        val scheduleItemId: Long,
        @ColumnInfo(name = "auditory_id", index = true)
        val auditoryId: Long
)