package com.pechuro.bsuirschedule.data.repository

import com.pechuro.bsuirschedule.data.common.BaseRepository
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

class BuildingRepositoryImpl(
        private val buildingDao: BuildingDao,
        private val specialityDao: SpecialityDao,
        private val api: BuildingApi
) : BaseRepository(), IBuildingRepository {

    override suspend fun getAllAuditories(forceUpdate: Boolean): Flow<List<Auditory>> {
        withContext(coroutineContext) {
            launch {
                if (forceUpdate || !isCached()) {
                    updateCache()
                }
            }
        }
        return getAllAuditoriesFromDao()
    }

    override suspend fun getAllAuditoryTypes(): Flow<List<AuditoryType>> =
            performDaoCall { buildingDao.getAllAuditoryTypes() }
                    .map { list ->
                        list.map { db ->
                            db.toDomainEntity()
                        }
                    }

    override suspend fun updateCache() {
        val loadedAuditories = loadAuditoriesFromApi()
        performDaoCall { buildingDao.deleteAll() }
        storeAuditories(loadedAuditories)
    }

    override suspend fun isCached(): Boolean =
            performDaoCall { buildingDao.isAuditoriesNotEmpty() }

    private suspend fun loadAuditoriesFromApi(): List<Auditory> =
            performApiCall { api.getAllAuditories() }
                    .map { dto ->
                        dto.toDomainEntity()
                    }

    private suspend fun getAllAuditoriesFromDao(): Flow<List<Auditory>> =
            performDaoCall { buildingDao.getAllAuditories() }
                    .map {
                        it.map { auditoryDB ->
                            val auditoryType = performDaoCall { buildingDao.getAuditoryTypeById(auditoryDB.auditoryTypeId) }
                            val building = performDaoCall { buildingDao.getBuildingById(auditoryDB.buildingId) }
                            val department = auditoryDB.departmentId?.let { departmentDb ->
                                performDaoCall { specialityDao.getDepartmentById(departmentDb) }
                            }
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
            performDaoCall {
                departmentDB?.let { department ->
                    performDaoCall { specialityDao.insert(department) }
                }
                buildingDao.insert(auditoryTypeDB)
                buildingDao.insert(buildingDB)
                buildingDao.insert(auditoryDB)
            }
        }
    }
}