package com.pechuro.bsuirschedule.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "all_schedules")
data class Schedule(
        @ColumnInfo(name = "name")
        val name: String,
        @ColumnInfo(name = "type")
        val type: Int,
        @ColumnInfo(name = "last_update")
        val lastUpdate: String?) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id: Long = 0
}
