package com.pechuro.bsuirschedule.remote.api

import com.pechuro.bsuirschedule.remote.dto.AnnouncementDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface AnnouncementApi {

    @GET("announcementEmployee")
    suspend fun getFromEmployee(
            @Query(value = "employeeId") employeeId: Long
    ): List<AnnouncementDTO>

    @GET("announcementDepartment")
    suspend fun getFromDepartment(
            @Query(value = "departmentId") departmentId: Long
    ): List<AnnouncementDTO>
}
