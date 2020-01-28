package com.pechuro.bsuirschedule.domain.repository

import com.pechuro.bsuirschedule.domain.entity.Department
import com.pechuro.bsuirschedule.domain.entity.Faculty
import com.pechuro.bsuirschedule.domain.entity.Speciality
import kotlinx.coroutines.flow.Flow

interface ISpecialityRepository {

    suspend fun getAllFaculties(forceUpdate: Boolean = false): Flow<List<Faculty>>

    suspend fun getAllDepartments(forceUpdate: Boolean = false): Flow<List<Department>>

    suspend fun getAllSpecialities(forceUpdate: Boolean = false): Flow<List<Speciality>>

    suspend fun updateCache()

    suspend fun isCached(): Boolean

    suspend fun addDepartment(department: Department)

    suspend fun getDepartmentById(id: Long): Department

    suspend fun getFacultyById(id: Long): Faculty?
}