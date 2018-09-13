package com.pechuro.bsuirschedule.repository.api

import com.pechuro.bsuirschedule.repository.entity.Employee
import com.pechuro.bsuirschedule.repository.entity.Group
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GroupApi {

    @GET("groups")
    fun get(): Single<List<Group>>
}

interface EmployeeApi {

    @GET("employees")
    fun get(): Single<List<Employee>>
}

interface ScheduleApi {

    @GET("studentGroup/lastUpdateDate")
    fun getLastUpdateDate(@Query(value = "studentGroup") studentGroup: String)
            : Single<LastUpdateResponse>

    @GET("studentGroup/schedule")
    fun getStudentSchedule(@Query(value = "studentGroup") studentGroup: String)
            : Single<Response>

    @GET("portal/employeeSchedule")
    fun getEmployeeSchedule(@Query(value = "employeeId") employeeId: String)
            : Single<Response>
}