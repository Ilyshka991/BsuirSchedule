package com.pechuro.bsuirschedule.domain.repository

import com.pechuro.bsuirschedule.domain.entity.Announcement
import com.pechuro.bsuirschedule.domain.entity.Department
import com.pechuro.bsuirschedule.domain.entity.Employee

interface IAnnouncementRepository {

    suspend fun getActualFromEmployee(employee: Employee): List<Announcement.EmployeeAnnouncement>

    suspend fun getActualFromDepartment(department: Department): List<Announcement.DepartmentAnnouncement>
}