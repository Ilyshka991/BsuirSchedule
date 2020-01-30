package com.pechuro.bsuirschedule.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
        tableName = "employee_schedule",
        primaryKeys = ["name", "type"],
        foreignKeys = [
            ForeignKey(
                    entity = EmployeeCached::class,
                    parentColumns = ["id"],
                    childColumns = ["employee_id"],
                    onDelete = ForeignKey.CASCADE)
        ]
)
data class EmployeeScheduleCached(
        @ColumnInfo(name = "name")
        val name: String,
        @ColumnInfo(name = "type")
        val type: Int,
        @ColumnInfo(name = "employee_id", index = true)
        val employeeId: Long
)