package com.pechuro.bsuirschedule.local.entity.building

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "building")
data class BuildingCached(
        @PrimaryKey
        @ColumnInfo(name = "id")
        val id: Long,
        @ColumnInfo(name = "name")
        val name: String
)