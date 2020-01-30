package com.pechuro.bsuirschedule.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
        tableName = "employee_schedule_item_auditory_join",
        primaryKeys = ["schedule_item_id", "auditory_id"],
        foreignKeys = [
            ForeignKey(
                    entity = EmployeeScheduleItemCached::class,
                    parentColumns = ["id"],
                    childColumns = ["schedule_item_id"],
                    onDelete = ForeignKey.CASCADE),
            ForeignKey(
                    entity = AuditoryCached::class,
                    parentColumns = ["id"],
                    childColumns = ["auditory_id"],
                    onDelete = ForeignKey.CASCADE)
        ]
)
data class EmployeeScheduleItemAuditoryCrossRef(
        @ColumnInfo(name = "schedule_item_id", index = true)
        val scheduleItemId: Long,
        @ColumnInfo(name = "auditory_id", index = true)
        val auditoryId: Long
)