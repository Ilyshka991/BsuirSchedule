package com.pechuro.bsuirschedule.domain.entity

data class Speciality(
        val id: Long,
        val faculty: Faculty,
        var name: String,
        val abbrev: String,
        val code: String
)