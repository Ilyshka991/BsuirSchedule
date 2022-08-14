package com.pechuro.bsuirschedule.local.entity.staff

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.pechuro.bsuirschedule.local.entity.education.DepartmentCached

@Entity(
    tableName = "employee",
    foreignKeys = [
        ForeignKey(
            entity = DepartmentCached::class,
            parentColumns = ["id"],
            childColumns = ["department_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class EmployeeCached(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "url_id")
    val urlId: String,
    @ColumnInfo(name = "first_name")
    val firstName: String,
    @ColumnInfo(name = "last_name")
    val lastName: String,
    @ColumnInfo(name = "middle_name")
    val middleName: String,
    @ColumnInfo(name = "abbreviation")
    val abbreviation: String,
    @ColumnInfo(name = "photo_link")
    val photoLink: String,
    @ColumnInfo(name = "department_id", index = true)
    val departmentId: Long?,
    @ColumnInfo(name = "rank")
    val rank: String
)
