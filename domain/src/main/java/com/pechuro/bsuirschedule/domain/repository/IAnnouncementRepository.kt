package com.pechuro.bsuirschedule.domain.repository

import com.pechuro.bsuirschedule.domain.entity.Announcement
import io.reactivex.Single

interface IAnnouncementRepository {

    fun getActualFromEmployee(employeeId: Long): Single<List<Announcement>>

    fun getActualFromDepartment(departmentId: Long): Single<List<Announcement>>
}