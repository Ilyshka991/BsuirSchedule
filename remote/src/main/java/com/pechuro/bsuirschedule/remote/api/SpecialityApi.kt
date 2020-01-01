package com.pechuro.bsuirschedule.remote.api

import com.pechuro.bsuirschedule.remote.dto.DepartmentDTO
import com.pechuro.bsuirschedule.remote.dto.FacultyDTO
import com.pechuro.bsuirschedule.remote.dto.SpecialityDTO
import io.reactivex.Single
import retrofit2.http.GET

interface SpecialityApi {

    @GET("faculties")
    fun getAllFaculties(): Single<List<FacultyDTO>>

    @GET("department")
    fun getAllDepartments(): Single<List<DepartmentDTO>>

    @GET("specialities")
    fun getAllSpecialities(): Single<List<SpecialityDTO>>
}
