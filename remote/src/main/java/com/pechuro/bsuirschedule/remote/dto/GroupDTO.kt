package com.pechuro.bsuirschedule.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GroupDTO(
        @Json(name = "id")
        val id: Long,
        @Json(name = "name")
        val number: String,
        @Json(name = "facultyId")
        val facultyId: Long,
        @Json(name = "course")
        val course: Int?,
        @Json(name = "specialityDepartmentEducationFormId")
        val specialityId: Long
)