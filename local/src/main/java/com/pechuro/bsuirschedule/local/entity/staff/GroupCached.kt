package com.pechuro.bsuirschedule.local.entity.staff

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.pechuro.bsuirschedule.local.entity.education.SpecialityCached

@Entity(
        tableName = "group",
        foreignKeys = [
                ForeignKey(
                        entity = SpecialityCached::class,
                        parentColumns = ["id"],
                        childColumns = ["speciality_id"],
                        onDelete = ForeignKey.CASCADE)
        ]
)
data class GroupCached(
        @PrimaryKey
        @ColumnInfo(name = "id")
        val id: Long,
        @ColumnInfo(name = "number")
        val number: String,
        @ColumnInfo(name = "speciality_id", index = true)
        val specialityId: Long,
        @ColumnInfo(name = "course")
        val course: Int
)