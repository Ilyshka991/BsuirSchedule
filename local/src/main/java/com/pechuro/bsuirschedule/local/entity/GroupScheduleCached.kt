package com.pechuro.bsuirschedule.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
        tableName = "group_schedule",
        primaryKeys = ["name", "type"],
        foreignKeys = [
            ForeignKey(
                    entity = GroupCached::class,
                    parentColumns = ["id"],
                    childColumns = ["group_id"],
                    onDelete = ForeignKey.CASCADE)
        ]
)
data class GroupScheduleCached(
        @ColumnInfo(name = "name")
        val name: String,
        @ColumnInfo(name = "type")
        val type: Int,
        @ColumnInfo(name = "group_id", index = true)
        val groupId: Long,
        @ColumnInfo(name = "last_update")
        val lastUpdate: String?
)