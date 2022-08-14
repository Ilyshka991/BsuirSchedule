package com.pechuro.bsuirschedule.local.entity.schedule.crossref

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.pechuro.bsuirschedule.local.entity.schedule.EmployeeItemExamCached
import com.pechuro.bsuirschedule.local.entity.staff.GroupCached

@Entity(
    tableName = "join_employee_exam_group",
    primaryKeys = ["schedule_item_id", "group_id"],
    foreignKeys = [
        ForeignKey(
            entity = EmployeeItemExamCached::class,
            parentColumns = ["id"],
            childColumns = ["schedule_item_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = GroupCached::class,
            parentColumns = ["id"],
            childColumns = ["group_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class EmployeeExamGroupCrossRef(
    @ColumnInfo(name = "schedule_item_id", index = true)
    val scheduleItemId: Long,
    @ColumnInfo(name = "group_id", index = true)
    val groupId: Long
)