package com.pechuro.bsuirschedule.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SpecialityDTO(
    @Json(name = "id")
    val id: Long,
    @Json(name = "name")
    val name: String,
    @Json(name = "abbrev")
    val abbreviation: String,
    @Json(name = "facultyId")
    val facultyId: Long,
    @Json(name = "code")
    val code: String,
    @Json(name = "educationForm")
    val educationForm: EducationFormDTO
)