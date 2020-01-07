package com.pechuro.bsuirschedule.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
        tableName = "speciality",
        foreignKeys = [
            ForeignKey(
                    entity = FacultyCached::class,
                    parentColumns = ["id"],
                    childColumns = ["faculty_id"],
                    onDelete = ForeignKey.CASCADE),
            ForeignKey(
                    entity = EducationFormCached::class,
                    parentColumns = ["id"],
                    childColumns = ["education_form_id"],
                    onDelete = ForeignKey.CASCADE)
        ]
)
data class SpecialityCached(
        @PrimaryKey
        @ColumnInfo(name = "id")
        val id: Long,
        @ColumnInfo(name = "faculty_id", index = true)
        val facultyId: Long?,
        @ColumnInfo(name = "education_form_id", index = true)
        val educationFormId: Long,
        @ColumnInfo(name = "name")
        var name: String,
        @ColumnInfo(name = "abbreviation")
        val abbreviation: String,
        @ColumnInfo(name = "code")
        val code: String
)