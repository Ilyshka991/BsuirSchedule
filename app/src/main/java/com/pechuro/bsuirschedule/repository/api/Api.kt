package com.pechuro.bsuirschedule.repository.api

import com.pechuro.bsuirschedule.repository.entity.Employee
import com.pechuro.bsuirschedule.repository.entity.Group
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GroupApi {

    @GET("v1/groups")
    fun getGroups(): Single<List<Group>>
}

interface EmployeeApi {
    @GET("v1/employees")
    fun getEmployees(): Single<List<Employee>>
}

interface ScheduleApi {

    @GET("v1/studentGroup/lastUpdateDate")
    fun getLastUpdateDate(@Query(value = "studentGroup") studentGroup: String)
            : Single<LastUpdateResponse>

    @GET("v1/studentGroup/schedule")
    fun getStudentSchedule(@Query(value = "studentGroup") studentGroup: String)
            : Single<Response>

    @GET("v1/portal/employeeSchedule")
    fun getEmployeeSchedule(@Query(value = "employeeId") employeeId: Int)
            : Single<Response>
}
