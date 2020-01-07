package com.pechuro.bsuirschedule.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "department")
data class DepartmentCached(
        @PrimaryKey
        @ColumnInfo(name = "id")
        val id: Long,
        @ColumnInfo(name = "name")
        var name: String,
        @ColumnInfo(name = "abbreviation")
        val abbreviation: String
)