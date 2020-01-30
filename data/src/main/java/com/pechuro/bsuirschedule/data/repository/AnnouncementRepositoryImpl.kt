package com.pechuro.bsuirschedule.data.repository

import com.pechuro.bsuirschedule.data.common.BaseRepository
import com.pechuro.bsuirschedule.domain.entity.Announcement.DepartmentAnnouncement
import com.pechuro.bsuirschedule.domain.entity.Announcement.EmployeeAnnouncement
import com.pechuro.bsuirschedule.domain.entity.Department
import com.pechuro.bsuirschedule.domain.entity.Employee
import com.pechuro.bsuirschedule.domain.repository.IAnnouncementRepository
import com.pechuro.bsuirschedule.remote.api.AnnouncementApi

class AnnouncementRepositoryImpl(
        private val api: AnnouncementApi
) : BaseRepository(), IAnnouncementRepository {

    override suspend fun getActualFromEmployee(employee: Employee): List<EmployeeAnnouncement> {
        TODO("not implemented")
    }

    override suspend fun getActualFromDepartment(department: Department): List<DepartmentAnnouncement> {
        TODO("not implemented")
    }
}