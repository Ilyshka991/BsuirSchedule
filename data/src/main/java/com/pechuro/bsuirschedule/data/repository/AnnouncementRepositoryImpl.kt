package com.pechuro.bsuirschedule.data.repository

import com.pechuro.bsuirschedule.domain.entity.Announcement
import com.pechuro.bsuirschedule.domain.repository.IAnnouncementRepository
import io.reactivex.Single

class AnnouncementRepositoryImpl : IAnnouncementRepository {

    override fun getActualFromEmployee(employeeId: Long): Single<List<Announcement>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getActualFromDepartment(departmentId: Long): Single<List<Announcement>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}