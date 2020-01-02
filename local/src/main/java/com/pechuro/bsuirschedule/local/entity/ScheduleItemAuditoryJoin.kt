package com.pechuro.bsuirschedule.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
        tableName = "schedule_item_auditory_join",
        primaryKeys = ["schedule_item_id", "auditory_id"],
        foreignKeys = [
            ForeignKey(
                    entity = ScheduleItemCached::class,
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
data class ScheduleItemAuditoryJoin(
        @ColumnInfo(name = "schedule_item_id", index = true)
        val scheduleItemId: Long,
        @ColumnInfo(name = "auditory_id", index = true)
        val auditoryId: Long
)