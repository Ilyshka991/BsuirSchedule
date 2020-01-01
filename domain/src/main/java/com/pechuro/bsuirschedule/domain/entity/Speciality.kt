package com.pechuro.bsuirschedule.domain.entity

data class Speciality(
        val id: Long,
        val faculty: Faculty,
        val educationForm: EducationForm,
        var name: String,
        val abbreviation: String,
        val code: String
)