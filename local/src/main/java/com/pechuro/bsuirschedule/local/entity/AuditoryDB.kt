package com.pechuro.bsuirschedule.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "auditory",
        foreignKeys = [
            ForeignKey(
                    entity = BuildingDB::class,
                    parentColumns = ["id"],
                    childColumns = ["building_id"],
                    onDelete = ForeignKey.CASCADE),
            ForeignKey(
                    entity = AuditoryTypeDB::class,
                    parentColumns = ["id"],
                    childColumns = ["auditory_type_id"],
                    onDelete = ForeignKey.CASCADE),
            ForeignKey(
                    entity = DepartmentDB::class,
                    parentColumns = ["id"],
                    childColumns = ["department_id"],
                    onDelete = ForeignKey.CASCADE)
        ]
)
data class AuditoryDB(
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
        val note: String?,
        @ColumnInfo(name = "capacity")
        val capacity: Int?
)