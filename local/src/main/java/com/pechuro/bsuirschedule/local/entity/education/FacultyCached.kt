package com.pechuro.bsuirschedule.local.entity.education

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "faculty")
data class FacultyCached(
        @PrimaryKey
        @ColumnInfo(name = "id")
        val id: Long,
        @ColumnInfo(name = "name")
        val name: String,
        @ColumnInfo(name = "abbreviation")
        val abbreviation: String
)