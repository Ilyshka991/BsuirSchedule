package com.pechuro.bsuirschedule.repository.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "all_groups")
data class Group(
        @PrimaryKey()
        var number: String,

        @ColumnInfo(name = "faculty_id")
        var facultyId: String,

        var course: Int?
)