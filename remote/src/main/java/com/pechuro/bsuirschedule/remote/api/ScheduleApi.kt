package com.pechuro.bsuirschedule.remote.api

import com.pechuro.bsuirschedule.remote.dto.LastUpdateDTO
import com.pechuro.bsuirschedule.remote.dto.ScheduleDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface ScheduleApi {

    @GET("week")
    suspend fun getCurrentWeek(): Int

    @GET("studentGroup/schedule")
    suspend fun getStudentSchedule(
            @Query(value = "studentGroup") name: String
    ): ScheduleDTO

    @GET("studentGroup/schedule")
    suspend fun getStudentSchedule(
            @Query(value = "id") id: Long
    ): ScheduleDTO

    @GET("portal/employeeSchedule")
    suspend fun getEmployeeSchedule(
            @Query(value = "employeeId") employeeId: Long
    ): ScheduleDTO

    @GET("studentGroup/lastUpdateDate")
    suspend fun getLastUpdateDate(
            @Query(value = "studentGroup") studentGroup: String
    ): LastUpdateDTO
}