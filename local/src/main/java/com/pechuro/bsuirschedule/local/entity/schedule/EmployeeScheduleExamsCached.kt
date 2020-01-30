package com.pechuro.bsuirschedule.local.entity.schedule

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.pechuro.bsuirschedule.local.entity.staff.EmployeeCached

@Entity(
        tableName = "employee_schedule_exams",
        foreignKeys = [
            ForeignKey(
                    entity = EmployeeCached::class,
                    parentColumns = ["id"],
                    childColumns = ["employee_id"],
                    onDelete = ForeignKey.NO_ACTION)
        ]
)
data class EmployeeScheduleExamsCached(
        @PrimaryKey
        @ColumnInfo(name = "name")
        val name: String,
        @ColumnInfo(name = "employee_id", index = true)
        val employeeId: Long
)