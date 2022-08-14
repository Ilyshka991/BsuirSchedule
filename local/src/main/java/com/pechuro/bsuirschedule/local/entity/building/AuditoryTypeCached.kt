package com.pechuro.bsuirschedule.local.entity.building

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "auditory_type")
data class AuditoryTypeCached(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "abbreviation")
    val abbreviation: String
)