package com.pechuro.bsuirschedule.data.repository

import com.pechuro.bsuirschedule.data.mappers.toDatabaseEntity
import com.pechuro.bsuirschedule.data.mappers.toDomainEntity
import com.pechuro.bsuirschedule.domain.entity.Department
import com.pechuro.bsuirschedule.domain.entity.EducationForm
import com.pechuro.bsuirschedule.domain.entity.Faculty
import com.pechuro.bsuirschedule.domain.entity.Speciality
import com.pechuro.bsuirschedule.domain.repository.ISpecialityRepository
import com.pechuro.bsuirschedule.local.dao.SpecialityDao
import com.pechuro.bsuirschedule.remote.api.SpecialityApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class SpecialityRepositoryImpl(
        private val api: SpecialityApi,
        private val dao: SpecialityDao
) : ISpecialityRepository {

    override suspend fun getAllFaculties(): Flow<List<Faculty>> {
        if (!dao.isFacultiesNotEmpty()) {
            val faculties = loadFacultiesFromApi()
            storeFaculties(faculties)
        }
        return getFacultiesFromDao()
    }

    override suspend fun getAllDepartments(): Flow<List<Department>> {
        if (!dao.isDepartmentsNotEmpty()) {
            val departments = loadDepartmentsFromApi()
            storeDepartments(departments)
        }
        return getDepartmentsFromDao()
    }

    override suspend fun getAllSpecialities(): Flow<List<Speciality>> {
        if (!dao.isSpecialitiesNotEmpty()) {
            val specialities = loadSpecialitiesFromApi()
            storeSpecialities(specialities)
        }
        return getSpecialitiesFromDao()
    }

    override suspend fun update() {
        val departments = loadDepartmentsFromApi()
        dao.deleteAllDepartments()
        storeDepartments(departments)
        val faculties = loadFacultiesFromApi()
        dao.deleteAllFaculties()
        storeFaculties(faculties)
        val specialities = loadSpecialitiesFromApi()
        dao.deleteAllSpecialities()
        storeSpecialities(specialities)
    }

    override suspend fun isStored() = withContext(Dispatchers.IO) {
        listOf(
                async { dao.isDepartmentsNotEmpty() },
                async { dao.isFacultiesNotEmpty() },
                async { dao.isSpecialitiesNotEmpty() }
        ).awaitAll().foldRight(true) { isNotEmpty, acc ->
            isNotEmpty and acc
        }
    }

    private suspend fun loadSpecialitiesFromApi(): List<Speciality> = api.getAllSpecialities()
            .map { dto ->
                val faculty = dao.getFacultyById(dto.facultyId)
                val educationForm = dto.educationForm.toDomainEntity()
                storeEducationForm(educationForm)
                dto.toDomainEntity(
                        faculty = faculty?.toDomainEntity()
                )
            }

    private suspend fun loadFacultiesFromApi(): List<Faculty> = api.getAllFaculties()
            .map { dto ->
                dto.toDomainEntity()
            }

    private suspend fun loadDepartmentsFromApi(): List<Department> = api.getAllDepartments()
            .map { dto ->
                dto.toDomainEntity()
            }

    private suspend fun getSpecialitiesFromDao() = dao.getAllSpecialities()
            .map { list ->
                list.map { specialityDB ->
                    val faculty = specialityDB.facultyId?.let { dao.getFacultyById(it) }
                    val educationForm = dao.getEducationFormById(specialityDB.educationFormId)
                    specialityDB.toDomainEntity(
                            faculty = faculty?.toDomainEntity(),
                            educationForm = educationForm.toDomainEntity()
                    )
                }
            }

    private fun getFacultiesFromDao() = dao.getAllFaculties()
            .map { list ->
                list.map { facultyDB ->
                    facultyDB.toDomainEntity()
                }
            }

    private fun getDepartmentsFromDao() = dao.getAllDepartments()
            .map { list ->
                list.map { departmentDB ->
                    departmentDB.toDomainEntity()
                }
            }

    private suspend fun storeFaculties(groups: List<Faculty>) {
        groups.map {
            it.toDatabaseEntity()
        }.run {
            dao.insertFacultyList(this)
        }
    }

    private suspend fun storeDepartments(groups: List<Department>) {
        groups.map {
            it.toDatabaseEntity()
        }.run {
            dao.insertDepartmentList(this)
        }
    }

    private suspend fun storeSpecialities(groups: List<Speciality>) {
        groups.map {
            it.toDatabaseEntity()
        }.run {
            dao.insertSpecialityList(this)
        }
    }

    private suspend fun storeEducationForm(educationForm: EducationForm) {
        dao.insert(educationForm.toDatabaseEntity())
    }
}