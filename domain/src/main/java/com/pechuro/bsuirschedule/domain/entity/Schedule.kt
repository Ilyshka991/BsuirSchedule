package com.pechuro.bsuirschedule.domain.entity

data class Schedule(
        val id: Long,
        val name: String,
        val type: Int,
        val lastUpdate: String?
)