package com.pechuro.bsuirschedule.domain.entity

data class Employee(
        val employeeId: String,
        val firstName: String?,
        val lastName: String,
        val middleName: String?,
        val fio: String,
        val photoLink: String?,
        val rank: String?
)
