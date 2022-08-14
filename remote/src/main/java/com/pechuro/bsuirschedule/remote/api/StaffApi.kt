package com.pechuro.bsuirschedule.remote.api

import com.pechuro.bsuirschedule.remote.dto.EmployeeDTO
import com.pechuro.bsuirschedule.remote.dto.GroupDTO
import retrofit2.http.GET

interface StaffApi {

    @GET("student-groups")
    suspend fun getAllGroups(): List<GroupDTO>

    @GET("employees/all")
    suspend fun getAllEmployees(): List<EmployeeDTO>
}
