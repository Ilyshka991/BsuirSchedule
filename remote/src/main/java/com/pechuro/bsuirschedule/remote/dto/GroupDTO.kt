package com.pechuro.bsuirschedule.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GroupDTO(
        @Json(name = "number")
        val number: String,
        @Json(name = "facultyId")
        val facultyId: String?,
        @Json(name = "course")
        val course: Int?
)