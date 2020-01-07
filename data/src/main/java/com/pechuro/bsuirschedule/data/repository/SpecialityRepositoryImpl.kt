package com.pechuro.bsuirschedule.data.repository

import com.pechuro.bsuirschedule.data.common.BaseRepository
import com.pechuro.bsuirschedule.data.mappers.toDatabaseEntity
import com.pechuro.bsuirschedule.data.mappers.toDomainEntity
import com.pechuro.bsuirschedule.domain.entity.Department
import com.pechuro.bsuirschedule.domain.entity.EducationForm
import com.pechuro.bsuirschedule.domain.entity.Faculty
import com.pechuro.bsuirschedule.domain.entity.Speciality
import com.pechuro.bsuirschedule.domain.repository.ISpecialityRepository
import com.pechuro.bsuirschedule.local.dao.SpecialityDao
import com.pechuro.bsuirschedule.remote.api.SpecialityApi
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.coroutineContext

class SpecialityRepositoryImpl(
        private val api: SpecialityApi,
        private val dao: SpecialityDao
) : BaseRepository(), ISpecialityRepository {

    override suspend fun getAllFaculties(forceUpdate: Boolean): Flow<List<Faculty>> {
        withContext(coroutineContext) {
            launch {
                if (performDaoCall { !dao.isFacultiesNotEmpty() }) {
                    val faculties = loadFacultiesFromApi()
                    storeFaculties(faculties)
                }
                if (forceUpdate) {
                    updateCache()
                }
            }
        }
        return getFacultiesFromDao()
    }

    override suspend fun getAllDepartments(forceUpdate: Boolean): Flow<List<Department>> {
        withContext(coroutineContext) {
            launch {
                if (performDaoCall { !dao.isDepartmentsNotEmpty() }) {
                    val departments = loadDepartmentsFromApi()
                    storeDepartments(departments)
                }
                if (forceUpdate) {
                    updateCache()
                }
            }
        }
        return getDepartmentsFromDao()
    }

    override suspend fun getAllSpecialities(forceUpdate: Boolean): Flow<List<Speciality>> {
        withContext(coroutineContext) {
            launch {
                if (performDaoCall { !dao.isSpecialitiesNotEmpty() }) {
                    val specialities = loadSpecialitiesFromApi()
                    storeSpecialities(specialities)
                }
                if (forceUpdate) {
                    updateCache()
                }
            }
        }
        return getSpecialitiesFromDao()
    }

    override suspend fun updateCache() {
        val departments = loadDepartmentsFromApi()
        performDaoCall { dao.deleteAllDepartments() }
        storeDepartments(departments)
        val faculties = loadFacultiesFromApi()
        performDaoCall { dao.deleteAllFaculties() }
        storeFaculties(faculties)
        val specialities = loadSpecialitiesFromApi()
        performDaoCall { dao.deleteAllSpecialities() }
        storeSpecialities(specialities)
    }

    override suspend fun isCached() = withContext(Dispatchers.IO) {
        listOf(
                async { performDaoCall { dao.isDepartmentsNotEmpty() } },
                async { performDaoCall { dao.isFacultiesNotEmpty() } },
                async { performDaoCall { dao.isSpecialitiesNotEmpty() } }
        ).awaitAll().foldRight(true) { isNotEmpty, acc ->
            isNotEmpty and acc
        }
    }

    private suspend fun loadSpecialitiesFromApi(): List<Speciality> =
            performApiCall { api.getAllSpecialities() }
                    .map { dto ->
                        val faculty = performDaoCall { dao.getFacultyById(dto.facultyId) }
                        val educationForm = dto.educationForm.toDomainEntity()
                        storeEducationForm(educationForm)
                        dto.toDomainEntity(
                                faculty = faculty?.toDomainEntity()
                        )
                    }

    private suspend fun loadFacultiesFromApi(): List<Faculty> =
            performApiCall { api.getAllFaculties() }
                    .map { dto ->
                        dto.toDomainEntity()
                    }

    private suspend fun loadDepartmentsFromApi(): List<Department> =
            performApiCall { api.getAllDepartments() }
                    .map { dto ->
                        dto.toDomainEntity()
                    }

    private suspend fun getSpecialitiesFromDao() = performDaoCall { dao.getAllSpecialities() }
            .map { list ->
                list.map { specialityDB ->
                    val faculty = specialityDB.facultyId?.let {
                        performDaoCall { dao.getFacultyById(it) }
                    }
                    val educationForm = performDaoCall {
                        dao.getEducationFormById(specialityDB.educationFormId)
                    }
                    specialityDB.toDomainEntity(
                            faculty = faculty?.toDomainEntity(),
                            educationForm = educationForm.toDomainEntity()
                    )
                }
            }

    private suspend fun getFacultiesFromDao() = performDaoCall { dao.getAllFaculties() }
            .map { list ->
                list.map { facultyDB ->
                    facultyDB.toDomainEntity()
                }
            }

    private suspend fun getDepartmentsFromDao() = performDaoCall { dao.getAllDepartments() }
            .map { list ->
                list.map { departmentDB ->
                    departmentDB.toDomainEntity()
                }
            }

    private suspend fun storeFaculties(groups: List<Faculty>) {
        groups.map {
            it.toDatabaseEntity()
        }.run {
            performDaoCall { dao.insertFaculties(this) }
        }
    }

    private suspend fun storeDepartments(groups: List<Department>) {
        groups.map {
            it.toDatabaseEntity()
        }.run {
            performDaoCall { dao.insertDepartments(this) }
        }
    }

    private suspend fun storeSpecialities(groups: List<Speciality>) {
        groups.map {
            it.toDatabaseEntity()
        }.run {
            performDaoCall { dao.insertSpecialities(this) }
        }
    }

    private suspend fun storeEducationForm(educationForm: EducationForm) {
        performDaoCall { dao.insert(educationForm.toDatabaseEntity()) }
    }
}