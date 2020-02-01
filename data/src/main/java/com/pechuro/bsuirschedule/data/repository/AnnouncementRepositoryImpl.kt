package com.pechuro.bsuirschedule.data.repository

import com.pechuro.bsuirschedule.data.common.BaseRepository
import com.pechuro.bsuirschedule.data.mappers.toDomainEntity
import com.pechuro.bsuirschedule.domain.entity.Announcement
import com.pechuro.bsuirschedule.domain.repository.IAnnouncementRepository
import com.pechuro.bsuirschedule.remote.api.AnnouncementApi

class AnnouncementRepositoryImpl(
        private val api: AnnouncementApi
) : BaseRepository(), IAnnouncementRepository {

    override suspend fun getActualFromEmployee(employeeId: Long): List<Announcement> {
        return performApiCall { api.getFromEmployee(employeeId) }
                .map { announcementItem ->
                    announcementItem.toDomainEntity()
                }
    }

    override suspend fun getActualFromDepartment(departmentId: Long): List<Announcement> {
        return performApiCall { api.getFromDepartment(departmentId) }
                .map { announcementItem ->
                    announcementItem.toDomainEntity()
                }
    }
}