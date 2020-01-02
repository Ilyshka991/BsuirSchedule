package com.pechuro.bsuirschedule.domain.repository

import com.pechuro.bsuirschedule.domain.entity.Department
import com.pechuro.bsuirschedule.domain.entity.Faculty
import com.pechuro.bsuirschedule.domain.entity.Speciality
import kotlinx.coroutines.flow.Flow

interface ISpecialityRepository {

    fun getAllFaculties(): Flow<List<Faculty>>

    fun getAllDepartments(): Flow<List<Department>>

    fun getAllSpecialities(): Flow<List<Speciality>>

    suspend fun update()

    suspend fun isStored(): Boolean
}