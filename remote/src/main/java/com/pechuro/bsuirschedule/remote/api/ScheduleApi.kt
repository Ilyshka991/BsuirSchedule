package com.pechuro.bsuirschedule.remote.api

import com.pechuro.bsuirschedule.remote.dto.LastUpdateDTO
import com.pechuro.bsuirschedule.remote.dto.ScheduleDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ScheduleApi {

    @GET("schedule")
    suspend fun getStudentSchedule(
        @Query(value = "studentGroup") groupNumber: String
    ): ScheduleDTO

    @GET("employees/schedule/{urlId}")
    suspend fun getEmployeeSchedule(
        @Path(value = "urlId") urlId: String
    ): ScheduleDTO

    @GET("last-update-date/student-group")
    suspend fun getLastUpdateDateStudent(
        @Query(value = "groupNumber") groupNumber: String
    ): LastUpdateDTO

    @GET("last-update-date/employee")
    suspend fun getLastUpdateDateEmployee(
        @Query(value = "url-id") urlId: String
    ): LastUpdateDTO
}