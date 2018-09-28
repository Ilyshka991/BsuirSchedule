package com.pechuro.bsuirschedule.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "all_schedules")
open class Schedule(val name: String,

                    val type: Int,

                    @ColumnInfo(name = "last_update")
                    val lastUpdate: String?) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id: Int = 0
}
