package com.pechuro.bsuirschedule.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
        tableName = "schedule_item_employee_join",
        primaryKeys = ["schedule_item_id", "employee_id"],
        foreignKeys = [
            ForeignKey(
                    entity = ScheduleItemDB::class,
                    parentColumns = ["id"],
                    childColumns = ["schedule_item_id"],
                    onDelete = ForeignKey.CASCADE),
            ForeignKey(
                    entity = EmployeeDB::class,
                    parentColumns = ["id"],
                    childColumns = ["employee_id"],
                    onDelete = ForeignKey.CASCADE)
        ]
)
data class ScheduleItemEmployeeJoinDB(
        @ColumnInfo(name = "schedule_item_id", index = true)
        val scheduleItemId: Long,
        @ColumnInfo(name = "employee_id", index = true)
        val employeeId: Long
)