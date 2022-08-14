package com.pechuro.bsuirschedule.data.repository

import com.pechuro.bsuirschedule.data.common.BaseRepository
import com.pechuro.bsuirschedule.data.mappers.toDatabaseEntity
import com.pechuro.bsuirschedule.data.mappers.toDomainEntity
import com.pechuro.bsuirschedule.domain.entity.Auditory
import com.pechuro.bsuirschedule.domain.repository.IBuildingRepository
import com.pechuro.bsuirschedule.domain.repository.ISpecialityRepository
import com.pechuro.bsuirschedule.local.dao.BuildingDao
import com.pechuro.bsuirschedule.remote.api.BuildingApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class BuildingRepositoryImpl(
    private val dao: BuildingDao,
    private val api: BuildingApi,
    private val specialityRepository: ISpecialityRepository
) : BaseRepository(), IBuildingRepository {

    override suspend fun getAllAuditories(forceUpdate: Boolean): Flow<List<Auditory>> {
        if (forceUpdate || !isCached()) {
            updateCache()
        }
        return getAllAuditoriesFromDao()
    }

    override suspend fun getAuditoryById(id: Long): Auditory {
        val auditoryCached = performDaoCall { dao.getAuditoryById(id) }
        val auditoryType = performDaoCall { dao.getAuditoryTypeById(auditoryCached.auditoryTypeId) }
        val building = performDaoCall { dao.getBuildingById(auditoryCached.buildingId) }
        val department = auditoryCached.departmentId?.let { departmentId ->
            specialityRepository.getDepartmentById(departmentId)
        }
        return auditoryCached.toDomainEntity(
            building = building.toDomainEntity(),
            auditoryType = auditoryType.toDomainEntity(),
            department = department
        )
    }

    override suspend fun updateCache() {
        val loadedAuditories = loadAuditoriesFromApi()
        storeAuditories(loadedAuditories)
    }

    override suspend fun isCached(): Boolean =
        performDaoCall { dao.isAuditoriesNotEmpty() }

    private suspend fun loadAuditoriesFromApi(): List<Auditory> =
        performApiCall { api.getAllAuditories() }
            .map { dto ->
                dto.toDomainEntity()
            }

    private suspend fun getAllAuditoriesFromDao(): Flow<List<Auditory>> =
        dao.getAllAuditories()
            .map {
                it.map { auditoryCached ->
                    val auditoryType =
                        performDaoCall { dao.getAuditoryTypeById(auditoryCached.auditoryTypeId) }
                    val building = performDaoCall { dao.getBuildingById(auditoryCached.buildingId) }
                    val department = auditoryCached.departmentId?.let { departmentId ->
                        specialityRepository.getDepartmentById(departmentId)
                    }
                    auditoryCached.toDomainEntity(
                        building = building.toDomainEntity(),
                        auditoryType = auditoryType.toDomainEntity(),
                        department = department
                    )
                }
            }
            .flowOn(Dispatchers.IO)

    private suspend fun storeAuditories(auditories: List<Auditory>) {
        auditories.forEach {
            it.department?.let { department ->
                specialityRepository.addDepartment(department)
            }
            val auditoryTypeCached = it.auditoryType.toDatabaseEntity()
            val departmentCached = it.department?.toDatabaseEntity()
            val buildingCached = it.building.toDatabaseEntity()
            val auditoryCached = it.toDatabaseEntity(
                auditoryType = auditoryTypeCached,
                building = buildingCached,
                department = departmentCached
            )
            performDaoCall {
                dao.insertOrUpdate(auditoryTypeCached)
                dao.insertOrUpdate(buildingCached)
                dao.insertOrUpdate(auditoryCached)
            }
        }
    }
}