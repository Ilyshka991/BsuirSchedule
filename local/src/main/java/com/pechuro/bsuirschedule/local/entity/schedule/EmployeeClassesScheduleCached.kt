package com.pechuro.bsuirschedule.local.entity.schedule

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.pechuro.bsuirschedule.local.entity.staff.EmployeeCached
import java.util.Date

@Entity(
    tableName = "employee_schedule_classes",
    foreignKeys = [
        ForeignKey(
            entity = EmployeeCached::class,
            parentColumns = ["id"],
            childColumns = ["employee_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class EmployeeClassesScheduleCached(
    @PrimaryKey
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "employee_id", index = true)
    val employeeId: Long,
    @ColumnInfo(name = "last_update")
    val lastUpdate: Date,
    @ColumnInfo(name = "not_remind_for_updates")
    val notRemindForUpdates: Boolean
)