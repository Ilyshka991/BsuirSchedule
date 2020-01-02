package com.pechuro.bsuirschedule.remote.api

import com.pechuro.bsuirschedule.remote.dto.DepartmentDTO
import com.pechuro.bsuirschedule.remote.dto.FacultyDTO
import com.pechuro.bsuirschedule.remote.dto.SpecialityDTO
import retrofit2.http.GET

interface SpecialityApi {

    @GET("faculties")
    suspend fun getAllFaculties(): List<FacultyDTO>

    @GET("department")
    suspend fun getAllDepartments(): List<DepartmentDTO>

    @GET("specialities")
    suspend fun getAllSpecialities(): List<SpecialityDTO>
}
