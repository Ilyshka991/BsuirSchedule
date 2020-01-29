package com.pechuro.bsuirschedule.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
        tableName = "schedule",
        primaryKeys = ["name", "type"]
)
data class ScheduleCached(
        @ColumnInfo(name = "name")
        val name: String,
        @ColumnInfo(name = "type")
        val type: Int,
        @ColumnInfo(name = "last_update")
        val lastUpdate: String?
)