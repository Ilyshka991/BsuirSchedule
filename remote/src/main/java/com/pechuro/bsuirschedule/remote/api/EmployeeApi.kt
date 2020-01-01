package com.pechuro.bsuirschedule.remote.api

import com.pechuro.bsuirschedule.remote.dto.EmployeeDTO
import io.reactivex.Single
import retrofit2.http.GET

interface EmployeeApi {

    @GET("employees")
    fun get(): Single<List<EmployeeDTO>>
}
