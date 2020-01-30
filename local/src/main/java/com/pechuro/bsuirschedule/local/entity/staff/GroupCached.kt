package com.pechuro.bsuirschedule.local.entity.staff

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.pechuro.bsuirschedule.local.entity.education.FacultyCached

@Entity(
        tableName = "group",
        foreignKeys = [
            ForeignKey(
                    entity = FacultyCached::class,
                    parentColumns = ["id"],
                    childColumns = ["faculty_id"],
                    onDelete = ForeignKey.CASCADE)
        ]
)
data class GroupCached(
        @PrimaryKey
        @ColumnInfo(name = "id")
        val id: Long,
        @ColumnInfo(name = "number")
        val number: String,
        @ColumnInfo(name = "faculty_id", index = true)
        val facultyId: Long?,
        @ColumnInfo(name = "course")
        val course: Int
)