package com.pechuro.bsuirschedule.local.entity.schedule.crossref

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.pechuro.bsuirschedule.local.entity.schedule.GroupItemExamCached
import com.pechuro.bsuirschedule.local.entity.staff.EmployeeCached

@Entity(
        tableName = "join_group_exam_employee",
        primaryKeys = ["schedule_item_id", "employee_id"],
        foreignKeys = [
            ForeignKey(
                    entity = GroupItemExamCached::class,
                    parentColumns = ["id"],
                    childColumns = ["schedule_item_id"],
                    onDelete = ForeignKey.CASCADE),
            ForeignKey(
                    entity = EmployeeCached::class,
                    parentColumns = ["id"],
                    childColumns = ["employee_id"],
                    onDelete = ForeignKey.CASCADE)
        ]
)
data class GroupExamEmployeeCrossRef(
        @ColumnInfo(name = "schedule_item_id", index = true)
        val scheduleItemId: Long,
        @ColumnInfo(name = "employee_id", index = true)
        val employeeId: Long
)