package com.pechuro.bsuirschedule.local.entity.building

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.pechuro.bsuirschedule.local.entity.education.DepartmentCached

@Entity(tableName = "auditory",
        foreignKeys = [
            ForeignKey(
                    entity = BuildingCached::class,
                    parentColumns = ["id"],
                    childColumns = ["building_id"],
                    onDelete = ForeignKey.CASCADE),
            ForeignKey(
                    entity = AuditoryTypeCached::class,
                    parentColumns = ["id"],
                    childColumns = ["auditory_type_id"],
                    onDelete = ForeignKey.CASCADE),
            ForeignKey(
                    entity = DepartmentCached::class,
                    parentColumns = ["id"],
                    childColumns = ["department_id"],
                    onDelete = ForeignKey.CASCADE)
        ]
)
data class AuditoryCached(
        @PrimaryKey
        @ColumnInfo(name = "id")
        val id: Long,
        @ColumnInfo(name = "building_id", index = true)
        val buildingId: Long,
        @ColumnInfo(name = "auditory_type_id", index = true)
        val auditoryTypeId: Long,
        @ColumnInfo(name = "department_id", index = true)
        val departmentId: Long?,
        @ColumnInfo(name = "name")
        val name: String,
        @ColumnInfo(name = "note")
        val note: String,
        @ColumnInfo(name = "capacity")
        val capacity: Int
)