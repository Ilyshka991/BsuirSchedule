package com.pechuro.bsuirschedule.data.repository

import com.pechuro.bsuirschedule.domain.entity.Announcement
import com.pechuro.bsuirschedule.domain.repository.IAnnouncementRepository
import com.pechuro.bsuirschedule.remote.api.AnnouncementApi
import io.reactivex.Single

class AnnouncementRepositoryImpl(private val api: AnnouncementApi) : IAnnouncementRepository {

    override fun getActualFromEmployee(employeeId: Long): Single<List<Announcement>> =
            api.getFromEmployee(employeeId).map { announcementList ->
                announcementList.map { announcementItem ->
                    announcementItem.run {
                        Announcement(
                                date = date,
                                content = content,
                                employeeName = null
                        )
                    }
                }
            }

    override fun getActualFromDepartment(departmentId: Long): Single<List<Announcement>> =
            api.getFromDepartment(departmentId).map { announcementList ->
                announcementList.map { announcementItem ->
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