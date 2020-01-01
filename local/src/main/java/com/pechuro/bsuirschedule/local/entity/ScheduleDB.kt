package com.pechuro.bsuirschedule.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule")
data class ScheduleDB(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        var id: Long = 0,
        @ColumnInfo(name = "name")
        val name: String,
        @ColumnInfo(name = "type")
        val type: Int,
        @ColumnInfo(name = "last_update")
        val lastUpdate: String?
)