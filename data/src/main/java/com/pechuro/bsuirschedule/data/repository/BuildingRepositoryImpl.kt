package com.pechuro.bsuirschedule.data.repository

import com.pechuro.bsuirschedule.data.mappers.toDatabaseEntity
import com.pechuro.bsuirschedule.data.mappers.toDomainEntity
import com.pechuro.bsuirschedule.domain.entity.Auditory
import com.pechuro.bsuirschedule.domain.entity.AuditoryType
import com.pechuro.bsuirschedule.domain.repository.IBuildingRepository
import com.pechuro.bsuirschedule.local.dao.BuildingDao
import com.pechuro.bsuirschedule.local.dao.SpecialityDao
import com.pechuro.bsuirschedule.remote.api.BuildingApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BuildingRepositoryImpl(
        private val buildingDao: BuildingDao,
        private val specialityDao: SpecialityDao,
        private val api: BuildingApi
) : IBuildingRepository {

    override suspend fun getAllAuditories(): Flow<List<Auditory>> {
        if (!isCached()) {
            val loadedAuditories = loadAuditoriesFromApi()
            storeAuditories(loadedAuditories)
        }
        return getAllAuditoriesFromDao()
    }

    override suspend fun getAllAuditoryTypes(): Flow<List<AuditoryType>> = buildingDao.getAllAuditoryTypes().map { list ->
        list.map { db ->
            db.toDomainEntity()
        }
    }

    override suspend fun updateCache() {
        val loadedAuditories = loadAuditoriesFromApi()
        buildingDao.deleteAll()
        storeAuditories(loadedAuditories)
    }

    override suspend fun isCached(): Boolean = buildingDao.isAuditoriesNotEmpty()

    private suspend fun loadAuditoriesFromApi(): List<Auditory> = api.getAllAuditories()
            .map { dto ->
                dto.toDomainEntity()
            }

    private fun getAllAuditoriesFromDao(): Flow<List<Auditory>> = buildingDao.getAllAuditories()
            .map {
                it.map { auditoryDB ->
                    val auditoryType = buildingDao.getAuditoryTypeById(auditoryDB.auditoryTypeId)
                    val building = buildingDao.getBuildingById(auditoryDB.buildingId)
                    val department = auditoryDB.departmentId?.let { departmentDb -> specialityDao.getDepartmentById(departmentDb) }
                    auditoryDB.toDomainEntity(
                            building = building.toDomainEntity(),
                            auditoryType = auditoryType.toDomainEntity(),
                            department = department?.toDomainEntity()
                    )
                }
            }

    private suspend fun storeAuditories(auditories: List<Auditory>) {
        auditories.forEach {
            val auditoryTypeDB = it.auditoryType.toDatabaseEntity()
            val departmentDB = it.department?.toDatabaseEntity()
            val buildingDB = it.building.toDatabaseEntity()
            val auditoryDB = it.toDatabaseEntity(
                    auditoryType = auditoryTypeDB,
                    building = buildingDB,
                    department = departmentDB
            )
            departmentDB?.let { depatrment -> specialityDao.insert(depatrment) }
            buildingDao.insert(auditoryTypeDB)
            buildingDao.insert(buildingDB)
            buildingDao.insert(auditoryDB)
        }
    }
}