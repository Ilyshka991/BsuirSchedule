package com.pechuro.bsuirschedule.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EmployeeDTO(
        @Json(name = "id")
        val id: Long,
        @Json(name = "firstName")
        val firstName: String,
        @Json(name = "lastName")
        val lastName: String,
        @Json(name = "middleName")
        val middleName: String?,
        @Json(name = "fio")
        val abbreviation: String,
        @Json(name = "photoLink")
        val photoLink: String?,
        @Json(name = "rank")
        val rank: String?
)
