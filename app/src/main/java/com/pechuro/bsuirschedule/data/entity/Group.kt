package com.pechuro.bsuirschedule.data.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
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