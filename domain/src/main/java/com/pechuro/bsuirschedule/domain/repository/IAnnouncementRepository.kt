package com.pechuro.bsuirschedule.domain.repository

import com.pechuro.bsuirschedule.domain.entity.Announcement

interface IAnnouncementRepository {

    suspend fun getActualFromEmployee(employeeId: Long): List<Announcement>

    suspend fun getActualFromDepartment(departmentId: Long): List<Announcement>
}