package com.pechuro.bsuirschedule.data.repository

import com.pechuro.bsuirschedule.domain.entity.Announcement
import com.pechuro.bsuirschedule.domain.repository.IAnnouncementRepository
import com.pechuro.bsuirschedule.remote.api.AnnouncementApi

class AnnouncementRepositoryImpl(private val api: AnnouncementApi) : IAnnouncementRepository {

    override suspend fun getActualFromEmployee(employeeId: Long): List<Announcement> {
        return api.getFromEmployee(employeeId).map { announcementItem ->
            announcementItem.run {
                Announcement(
                        date = date,
                        content = content,
                        employeeName = null
                )
            }
        }
    }

    override suspend fun getActualFromDepartment(departmentId: Long): List<Announcement> {
        return api.getFromDepartment(departmentId).map { announcementItem ->
            announcementItem.run {
                Announcement(
                        date = date,
                        content = content,
                        employeeName = employeeName
                )
            }
        }
    }
}