package com.pechuro.bsuirschedule.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "all_groups")
data class Group(
        @PrimaryKey
        @ColumnInfo(name = "number")
        val number: String,
        @ColumnInfo(name = "faculty_id")
        val facultyId: String?,
        @ColumnInfo(name = "course")
        val course: Int?
)