package com.pechuro.bsuirschedule.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "all_employees")
data class Employee(
        @PrimaryKey
        @ColumnInfo(name = "employee_id")
        val employeeId: String,
        @ColumnInfo(name = "first_name")
        val firstName: String?,
        @ColumnInfo(name = "last_name")
        val lastName: String,
        @ColumnInfo(name = "middle_name")
        val middleName: String?,
        @ColumnInfo(name = "fio")
        val fio: String,
        @ColumnInfo(name = "photo_link")
        val photoLink: String?,
        @ColumnInfo(name = "rank")
        val rank: String?
)
