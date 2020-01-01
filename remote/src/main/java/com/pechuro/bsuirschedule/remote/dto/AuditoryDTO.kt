package com.pechuro.bsuirschedule.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuditoryDTO(
        @Json(name = "id")
        val id: Long,
        @Json(name = "name")
        val name: String,
        @Json(name = "note")
        val note: String?,
        @Json(name = "capacity")
        val capacity: Int?,
        @Json(name = "auditoryType")
        val auditoryType: AuditoryTypeDTO,
        @Json(name = "buildingNumber")
        val buildingNumber: BuildingDTO,
        @Json(name = "department")
        val department: DepartmentDTO
)