package com.pechuro.bsuirschedule.data.repository

import com.pechuro.bsuirschedule.data.common.BaseRepository
import com.pechuro.bsuirschedule.data.mappers.toDatabaseEntity
import com.pechuro.bsuirschedule.data.mappers.toDomainEntity
import com.pechuro.bsuirschedule.domain.entity.Department
import com.pechuro.bsuirschedule.domain.entity.EducationForm
import com.pechuro.bsuirschedule.domain.entity.Faculty
import com.pechuro.bsuirschedule.domain.entity.Speciality
import com.pechuro.bsuirschedule.domain.exception.DataSourceException
import com.pechuro.bsuirschedule.domain.repository.ISpecialityRepository
import com.pechuro.bsuirschedule.local.dao.SpecialityDao
import com.pechuro.bsuirschedule.remote.api.SpecialityApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class SpecialityRepositoryImpl(
        private val api: SpecialityApi,
        private val dao: SpecialityDao
) : BaseRepository(), ISpecialityRepository {

    override suspend fun getAllFaculties(forceUpdate: Boolean): Flow<List<Faculty>> {
        val isFacultiesCached = performDaoCall { dao.isFacultiesNotEmpty() }
        if (forceUpdate || !isFacultiesCached) {
            val faculties = loadFacultiesFromApi()
            storeFaculties(faculties)
        }
        return getFacultiesFromDao()
    }

    override suspend fun getAllDepartments(forceUpdate: Boolean): Flow<List<Department>> {
        val isDepartmentsCached = performDaoCall { dao.isDepartmentsNotEmpty() }
        if (forceUpdate || !isDepartmentsCached) {
            val departments = loadDepartmentsFromApi()
            storeDepartments(departments)
        }
        return getDepartmentsFromDao()
    }

    override suspend fun getAllSpecialities(forceUpdate: Boolean): Flow<List<Speciality>> {
        val isSpecialitiesCached = performDaoCall { dao.isSpecialitiesNotEmpty() }
        if (forceUpdate || !isSpecialitiesCached) {
            val specialities = loadSpecialitiesFromApi()
            storeSpecialities(specialities)
        }
        return getSpecialitiesFromDao()
    }

    override suspend fun updateCache() {
        val departments = loadDepartmentsFromApi()
        storeDepartments(departments)
        val faculties = loadFacultiesFromApi()
        storeFaculties(faculties)
        val specialities = loadSpecialitiesFromApi()
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

    override suspend fun addDepartment(department: Department) {
        performDaoCall { dao.insertOrUpdate(department.toDatabaseEntity()) }
    }

    override suspend fun getDepartmentById(id: Long): Department = performDaoCall {
        dao.getDepartmentById(id)
    }.toDomainEntity()

    override suspend fun getFacultyById(id: Long): Faculty = performDaoCall {
        dao.getFacultyById(id)
    }?.toDomainEntity() ?: throw DataSourceException.InvalidData

    override suspend fun getEducationFormById(id: Long): EducationForm = performDaoCall {
        dao.getEducationFormById(id)
    }.toDomainEntity()

    override suspend fun getSpecialityById(id: Long): Speciality {
        val specialityCached = performDaoCall { dao.getSpecialityById(id) }
        val faculty = specialityCached.facultyId?.let { performDaoCall { dao.getFacultyById(it) } }
        val educationForm = performDaoCall { dao.getEducationFormById(specialityCached.educationFormId) }
        return specialityCached.toDomainEntity(
                faculty = faculty?.toDomainEntity(),
                educationForm = educationForm.toDomainEntity()
        )
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

    private suspend fun getSpecialitiesFromDao() = dao.getAllSpecialities()
            .map { list ->
                withContext(Dispatchers.IO) {
                    list.map { specialityCached ->
                        async {
                            val faculty = specialityCached.facultyId?.let {
                                performDaoCall { dao.getFacultyById(it) }
                            }
                            val educationForm = performDaoCall {
                                dao.getEducationFormById(specialityCached.educationFormId)
                            }
                            specialityCached.toDomainEntity(
                                    faculty = faculty?.toDomainEntity(),
                                    educationForm = educationForm.toDomainEntity()
                            )
                        }
                    }
                }.awaitAll()
            }
            .flowOn(Dispatchers.IO)

    private suspend fun getFacultiesFromDao() = dao.getAllFaculties()
            .map { list ->
                withContext(Dispatchers.IO) {
                    list.map { facultyCached ->
                        async {
                            facultyCached.toDomainEntity()
                        }
                    }
                }.awaitAll()
            }
            .flowOn(Dispatchers.IO)

    private suspend fun getDepartmentsFromDao() = dao.getAllDepartments()
            .map { list ->
                list.map { departmentCached ->
                    departmentCached.toDomainEntity()
                }
            }
            .flowOn(Dispatchers.IO)

    private suspend fun storeFaculties(groups: List<Faculty>) {
        groups.map {
            it.toDatabaseEntity()
        }.run {
            performDaoCall { dao.insertOrUpdateFaculties(this) }
        }
    }

    private suspend fun storeDepartments(groups: List<Department>) {
        groups.map {
            it.toDatabaseEntity()
        }.run {
            performDaoCall { dao.insertOrUpdateDepartments(this) }
        }
    }

    private suspend fun storeSpecialities(groups: List<Speciality>) {
        groups.map {
            it.toDatabaseEntity()
        }.run {
            performDaoCall { dao.insertOrUpdateSpecialities(this) }
        }
    }

    private suspend fun storeEducationForm(educationForm: EducationForm) {
        performDaoCall { dao.insertOrUpdate(educationForm.toDatabaseEntity()) }
    }
}