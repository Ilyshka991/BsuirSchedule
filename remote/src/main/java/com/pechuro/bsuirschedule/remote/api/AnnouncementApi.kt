package com.pechuro.bsuirschedule.remote.api

import com.pechuro.bsuirschedule.remote.dto.AnnouncementDTO
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface AnnouncementApi {

    @GET("announcementEmployee")
    fun getFromEmployee(
            @Query(value = "employeeId") employeeId: Long
    ): Single<List<AnnouncementDTO>>

    @GET("announcementDepartment")
    fun getFromDepartment(
            @Query(value = "departmentId") departmentId: Long
    ): Single<List<AnnouncementDTO>>
}
