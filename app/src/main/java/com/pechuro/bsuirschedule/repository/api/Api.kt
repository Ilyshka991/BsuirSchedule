package com.pechuro.bsuirschedule.repository.api

import com.pechuro.bsuirschedule.repository.entity.Employee
import com.pechuro.bsuirschedule.repository.entity.Group
import com.pechuro.bsuirschedule.repository.entity.complex.StudentClasses
import com.pechuro.bsuirschedule.repository.entity.item.StudentClassItem
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GroupApi {

    @GET("v1/groups")
    fun getGroups(): Single<List<Group>>
}

interface EmployeeApi {
    @GET("v1/employees")
    fun getEmployees(): Single<List<Employee>>
}

interface StudentScheduleApi {

    @GET("v1/studentGroup/lastUpdateDate")
    fun getLastUpdateDate(): Single<String>

    @GET("v1/studentGroup/schedule")
    fun getSchedule(@Query(value = "studentGroup") studentGroup: String): Single<List<StudentClassItem>>

}
