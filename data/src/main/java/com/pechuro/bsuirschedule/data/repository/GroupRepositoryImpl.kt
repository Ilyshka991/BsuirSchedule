package com.pechuro.bsuirschedule.data.repository

import com.pechuro.bsuirschedule.data.mappers.toDatabaseEntity
import com.pechuro.bsuirschedule.data.mappers.toDomainEntity
import com.pechuro.bsuirschedule.domain.entity.Group
import com.pechuro.bsuirschedule.domain.repository.IGroupRepository
import com.pechuro.bsuirschedule.local.dao.GroupDao
import com.pechuro.bsuirschedule.local.dao.SpecialityDao
import com.pechuro.bsuirschedule.remote.api.StaffApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GroupRepositoryImpl(
        private val groupDao: GroupDao,
        private val specialityDao: SpecialityDao,
        private val api: StaffApi
) : IGroupRepository {

    override suspend fun getAll(): Flow<List<Group>> {
        if (!isStored()) {
            val loadedGroups = loadGroupsFromApi()
            storeGroups(loadedGroups)
        }
        return getGroupsFromDao()
    }

    override suspend fun getByNumber(number: String): Group {
        val groupDB = groupDao.getByNumber(number)
        val faculty = groupDB.facultyId?.let { specialityDao.getFacultyById(it) }
        return groupDB.toDomainEntity(faculty = faculty?.toDomainEntity())
    }

    override suspend fun update() {
        val loadedGroups = loadGroupsFromApi()
        deleteAll()
        storeGroups(loadedGroups)
    }

    override suspend fun deleteAll() {
        groupDao.deleteAll()
    }

    override suspend fun isStored(): Boolean = groupDao.isNotEmpty()

    private suspend fun loadGroupsFromApi(): List<Group> = api.getAllGroups()
            .map { dto ->
                val faculty = dto.facultyId?.let { specialityDao.getFacultyById(it) }
                dto.toDomainEntity(
                        faculty = faculty?.toDomainEntity()
                )
            }

    private fun getGroupsFromDao() = groupDao.getAll().map { list ->
        list.map { groupDb ->
            val faculty = groupDb.facultyId?.let { specialityDao.getFacultyById(it) }
            groupDb.toDomainEntity(
                    faculty = faculty?.toDomainEntity()
            )
        }
    }

    private suspend fun storeGroups(groups: List<Group>) {
        groups.map {
            it.toDatabaseEntity()
        }.run {
            groupDao.insert(this)
        }
    }
}