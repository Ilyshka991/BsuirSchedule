package com.pechuro.bsuirschedule.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
        tableName = "schedule_item_group_join",
        primaryKeys = ["schedule_item_id", "group_id"],
        foreignKeys = [
            ForeignKey(
                    entity = ScheduleItemCached::class,
                    parentColumns = ["id"],
                    childColumns = ["schedule_item_id"],
                    onDelete = ForeignKey.CASCADE),
            ForeignKey(
                    entity = GroupCached::class,
                    parentColumns = ["id"],
                    childColumns = ["group_id"],
                    onDelete = ForeignKey.CASCADE)
        ]
)
data class ScheduleItemGroupCrossRef(
        @ColumnInfo(name = "schedule_item_id", index = true)
        val scheduleItemId: Long,
        @ColumnInfo(name = "group_id", index = true)
        val groupId: Long
)