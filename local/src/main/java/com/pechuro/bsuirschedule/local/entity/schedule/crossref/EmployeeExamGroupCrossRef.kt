package com.pechuro.bsuirschedule.local.entity.schedule.crossref

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.pechuro.bsuirschedule.local.entity.schedule.EmployeeExamCached
import com.pechuro.bsuirschedule.local.entity.staff.GroupCached

@Entity(
        tableName = "employee_exam_group_join",
        primaryKeys = ["schedule_item_id", "group_id"],
        foreignKeys = [
            ForeignKey(
                    entity = EmployeeExamCached::class,
                    parentColumns = ["id"],
                    childColumns = ["schedule_item_id"],
                    onDelete = ForeignKey.CASCADE),
            ForeignKey(
                    entity = GroupCached::class,
                    parentColumns = ["id"],
                    childColumns = ["group_id"],
                    onDelete = ForeignKey.NO_ACTION)
        ]
)
data class EmployeeExamGroupCrossRef(
        @ColumnInfo(name = "schedule_item_id", index = true)
        val scheduleItemId: Long,
        @ColumnInfo(name = "group_id", index = true)
        val groupId: Long
)