package com.pechuro.bsuirschedule.repository.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "all_schedules")
open class Schedule(val name: String,

                    val type: Int,

                    @ColumnInfo(name = "last_update")
                    val lastUpdate: String) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id: Int = 0
}
