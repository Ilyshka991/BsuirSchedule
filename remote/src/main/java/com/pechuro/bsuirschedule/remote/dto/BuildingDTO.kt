package com.pechuro.bsuirschedule.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BuildingDTO(
        @Json(name = "id")
        val id: Long,
        @Json(name = "name")
        val name: String
)