package com.pechuro.bsuirschedule.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuditoryTypeDTO(
        @Json(name = "id")
        val id: Long,
        @Json(name = "name")
        val name: String,
        @Json(name = "abbrev")
        val abbreviation: String
)