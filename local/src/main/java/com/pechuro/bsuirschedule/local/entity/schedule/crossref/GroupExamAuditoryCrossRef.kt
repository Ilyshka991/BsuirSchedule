package com.pechuro.bsuirschedule.local.entity.schedule.crossref

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.pechuro.bsuirschedule.local.entity.building.AuditoryCached
import com.pechuro.bsuirschedule.local.entity.schedule.GroupExamCached

@Entity(
        tableName = "group_exam_auditory_join",
        primaryKeys = ["schedule_item_id", "auditory_id"],
        foreignKeys = [
            ForeignKey(
                    entity = GroupExamCached::class,
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
data class GroupExamAuditoryCrossRef(
        @ColumnInfo(name = "schedule_item_id", index = true)
        val scheduleItemId: Long,
        @ColumnInfo(name = "auditory_id", index = true)
        val auditoryId: Long
)