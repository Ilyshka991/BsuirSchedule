package com.pechuro.bsuirschedule.remote.api

import com.pechuro.bsuirschedule.remote.dto.EmployeeDTO
import com.pechuro.bsuirschedule.remote.dto.GroupDTO
import io.reactivex.Single
import retrofit2.http.GET

interface StaffApi {

    @GET("groups")
    fun getAllGroups(): Single<List<GroupDTO>>

    @GET("employees")
    fun getAllEmployees(): Single<List<EmployeeDTO>>
}
