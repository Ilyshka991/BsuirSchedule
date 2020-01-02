package com.pechuro.bsuirschedule.data.repository

import com.pechuro.bsuirschedule.domain.entity.Auditory
import com.pechuro.bsuirschedule.domain.entity.AuditoryType
import com.pechuro.bsuirschedule.domain.entity.Building
import com.pechuro.bsuirschedule.domain.entity.Department
import com.pechuro.bsuirschedule.domain.repository.IBuildingRepository
import com.pechuro.bsuirschedule.local.dao.BuildingDao
import com.pechuro.bsuirschedule.local.dao.SpecialityDao
import com.pechuro.bsuirschedule.local.entity.AuditoryDB
import com.pechuro.bsuirschedule.local.entity.AuditoryTypeDB
import com.pechuro.bsuirschedule.local.entity.BuildingDB
import com.pechuro.bsuirschedule.local.entity.DepartmentDB
import com.pechuro.bsuirschedule.remote.api.BuildingApi
import com.pechuro.bsuirschedule.remote.dto.AuditoryDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BuildingRepositoryImpl(
        private val buildingDao: BuildingDao,
        private val specialityDao: SpecialityDao,
        private val api: BuildingApi
) : IBuildingRepository {

    override suspend fun getAllAuditories(): Flow<List<Auditory>> {
        if (!buildingDao.isAuditoriesNotEmpty()) {
            val loadedAuditories = loadAllAuditoriesFromApi()
            storeAllAuditories(loadedAuditories)
        }
        return getAllAuditoriesFromDao()
    }

    override suspend fun getAllAuditoryTypes(): Flow<List<AuditoryType>> = buildingDao.getAllAuditoryTypes().map { list ->
        list.map { db ->
            db.toAuditoryTypeEntity()
        }
    }

    override suspend fun update() {
        val loadedAuditories = loadAllAuditoriesFromApi()
        buildingDao.deleteAll()
        storeAllAuditories(loadedAuditories)
    }

    override suspend fun isStored(): Boolean = buildingDao.isAuditoriesNotEmpty()

    private suspend fun loadAllAuditoriesFromApi(): List<Auditory> = api.getAllAuditories()
            .map { dto ->
                dto.toDomainEntity()
            }

    private fun getAllAuditoriesFromDao(): Flow<List<Auditory>> = buildingDao.getAllAuditories()
            .map {
                it.map { auditory ->
                    val auditoryType = buildingDao.getAuditoryTypeById(auditory.auditoryTypeId)
                    val building = buildingDao.getBuildingById(auditory.buildingId)
                    val department = auditory.departmentId?.let { departmentDb -> specialityDao.getDepartmentById(departmentDb) }
                    Auditory(
                            id = auditory.id,
                            name = auditory.name,
                            note = auditory.note,
                            capacity = auditory.capacity,
                            building = building.toBuildingEntity(),
                            auditoryType = auditoryType.toAuditoryTypeEntity(),
                            department = department?.toDepartmentEntity()
                    )
                }
            }

    private suspend fun storeAllAuditories(auditories: List<Auditory>) {
        auditories.forEach {
            val auditoryTypeDB = it.toAuditoryTypeDb()
            val departmentDB = it.toDepartmentDb()
            val buildingDB = it.toBuildingDb()
            val auditoryDB = AuditoryDB(
                    id = it.id,
                    buildingId = buildingDB.id,
                    auditoryTypeId = auditoryTypeDB.id,
                    departmentId = departmentDB?.id,
                    name = it.name,
                    note = it.note,
                    capacity = it.capacity
            )
            departmentDB?.let { depatrment -> specialityDao.insert(depatrment) }
            buildingDao.insert(auditoryTypeDB)
            buildingDao.insert(buildingDB)
            buildingDao.insert(auditoryDB)
        }
    }

    private fun AuditoryDTO.toDomainEntity() = run {
        val building = buildingNumber.run {
            Building(
                    id = id,
                    name = name
            )
        }
        val auditoryType = auditoryType.run {
            AuditoryType(
                    id = id,
                    name = name,
                    abbreviation = abbreviation
            )
        }
        val department = department?.run {
            Department(
                    id = id,
                    name = name,
                    abbreviation = abbreviation
            )
        }
        Auditory(
                id = id,
                name = name,
                note = note,
                capacity = capacity,
                building = building,
                auditoryType = auditoryType,
                department = department
        )
    }

    private fun Auditory.toDepartmentDb() = department?.run {
        DepartmentDB(
                id = id,
                name = name,
                abbreviation = abbreviation
        )
    }

    private fun Auditory.toAuditoryTypeDb() = auditoryType.run {
        AuditoryTypeDB(
                id = id,
                name = name,
                abbreviation = abbreviation
        )
    }

    private fun Auditory.toBuildingDb() = building.run {
        BuildingDB(
                id = id,
                name = name
        )
    }

    private fun DepartmentDB.toDepartmentEntity() = run {
        Department(
                id = id,
                name = name,
                abbreviation = abbreviation
        )
    }

    private fun AuditoryTypeDB.toAuditoryTypeEntity() = run {
        AuditoryType(
                id = id,
                name = name,
                abbreviation = abbreviation
        )
    }

    private fun BuildingDB.toBuildingEntity() = run {
        Building(
                id = id,
                name = name
        )
    }
}