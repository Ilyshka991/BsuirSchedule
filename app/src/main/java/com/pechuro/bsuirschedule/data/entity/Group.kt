package com.pechuro.bsuirschedule.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "all_groups")
data class Group(
        @PrimaryKey()
        @SerializedName("name")
        var number: String,

        @ColumnInfo(name = "faculty_id")
        var facultyId: String?,

        var course: Int?
)