package com.pechuro.bsuirschedule.data.repository

import com.pechuro.bsuirschedule.domain.entity.Department
import com.pechuro.bsuirschedule.domain.entity.Faculty
import com.pechuro.bsuirschedule.domain.entity.Speciality
import com.pechuro.bsuirschedule.domain.repository.ISpecialityRepository
import kotlinx.coroutines.flow.Flow

class SpecialityRepositoryImpl : ISpecialityRepository {

    override suspend fun getAllFaculties(): Flow<List<Faculty>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getAllDepartments(): Flow<List<Department>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getAllSpecialities(): Flow<List<Speciality>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun update() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun isStored(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}