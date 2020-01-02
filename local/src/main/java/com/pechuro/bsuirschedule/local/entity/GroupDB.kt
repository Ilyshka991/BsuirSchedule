package com.pechuro.bsuirschedule.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
        tableName = "group",
        foreignKeys = [
            ForeignKey(
                    entity = FacultyDB::class,
                    parentColumns = ["id"],
                    childColumns = ["faculty_id"],
                    onDelete = ForeignKey.CASCADE)
        ]
)
data class GroupDB(
        @PrimaryKey
        @ColumnInfo(name = "id")
        val id: Long,
        @ColumnInfo(name = "number")
        val number: String,
        @ColumnInfo(name = "faculty_id", index = true)
        val facultyId: Long?,
        @ColumnInfo(name = "course")
        val course: Int?
)