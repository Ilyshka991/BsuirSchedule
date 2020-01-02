package com.pechuro.bsuirschedule.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employee")
data class EmployeeCached(
        @PrimaryKey
        @ColumnInfo(name = "id")
        val id: Long,
        @ColumnInfo(name = "first_name")
        val firstName: String,
        @ColumnInfo(name = "last_name")
        val lastName: String,
        @ColumnInfo(name = "middle_name")
        val middleName: String?,
        @ColumnInfo(name = "abbreviation")
        val abbreviation: String,
        @ColumnInfo(name = "photo_link")
        val photoLink: String?,
        @ColumnInfo(name = "rank")
        val rank: String?
)
