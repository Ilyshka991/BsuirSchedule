package com.pechuro.bsuirschedule.domain.entity

data class Auditory(
        val id: Long,
        val building: Building,
        val auditoryType: AuditoryType,
        val department: Department,
        val name: String,
        val note: String?,
        val capacity: Int?
)