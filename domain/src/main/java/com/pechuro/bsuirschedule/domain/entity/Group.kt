package com.pechuro.bsuirschedule.domain.entity

data class Group(
        val id: Long,
        val number: String,
        val faculty: Faculty?,
        val course: Int
)