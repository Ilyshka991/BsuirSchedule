package com.pechuro.bsuirschedule.local.entity.schedule

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.pechuro.bsuirschedule.local.entity.staff.GroupCached
import java.util.*

@Entity(
        tableName = "group_schedule_exam",
        foreignKeys = [
            ForeignKey(
                    entity = GroupCached::class,
                    parentColumns = ["id"],
                    childColumns = ["group_id"],
                    onDelete = ForeignKey.CASCADE)
        ]
)
data class GroupExamScheduleCached(
        @PrimaryKey
        @ColumnInfo(name = "name")
        val name: String,
        @ColumnInfo(name = "group_id", index = true)
        val groupId: Long,
        @ColumnInfo(name = "last_update")
        val lastUpdate: Date
)