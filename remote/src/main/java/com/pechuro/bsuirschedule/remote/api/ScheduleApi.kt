package com.pechuro.bsuirschedule.remote.api

import com.pechuro.bsuirschedule.remote.dto.LastUpdateDTO
import com.pechuro.bsuirschedule.remote.dto.ScheduleDTO
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ScheduleApi {

    @GET("studentGroup/lastUpdateDate")
    fun getLastUpdateDate(
            @Query(value = "studentGroup") studentGroup: String
    ): Single<LastUpdateDTO>

    @GET("studentGroup/schedule")
    fun getStudentSchedule(
            @Query(value = "studentGroup") studentGroup: String
    ): Single<ScheduleDTO>

    @GET("portal/employeeSchedule")
    fun getEmployeeSchedule(
            @Query(value = "employeeId") employeeId: String
    ): Single<ScheduleDTO>
}