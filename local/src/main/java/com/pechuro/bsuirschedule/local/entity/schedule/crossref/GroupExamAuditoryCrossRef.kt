package com.pechuro.bsuirschedule.local.entity.schedule.crossref

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.pechuro.bsuirschedule.local.entity.building.AuditoryCached
import com.pechuro.bsuirschedule.local.entity.schedule.GroupItemExamCached

@Entity(
    tableName = "join_group_exam_auditory",
    primaryKeys = ["schedule_item_id", "auditory_id"],
    foreignKeys = [
        ForeignKey(
            entity = GroupItemExamCached::class,
            parentColumns = ["id"],
            childColumns = ["schedule_item_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AuditoryCached::class,
            parentColumns = ["id"],
            childColumns = ["auditory_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class GroupExamAuditoryCrossRef(
    @ColumnInfo(name = "schedule_item_id", index = true)
    val scheduleItemId: Long,
    @ColumnInfo(name = "auditory_id", index = true)
    val auditoryId: Long
)