package com.pechuro.bsuirschedule.data.repository

import com.pechuro.bsuirschedule.data.common.BaseRepository
import com.pechuro.bsuirschedule.data.mappers.toDatabaseEntity
import com.pechuro.bsuirschedule.data.mappers.toDomainEntity
import com.pechuro.bsuirschedule.domain.entity.Group
import com.pechuro.bsuirschedule.domain.repository.IGroupRepository
import com.pechuro.bsuirschedule.local.dao.GroupDao
import com.pechuro.bsuirschedule.local.dao.SpecialityDao
import com.pechuro.bsuirschedule.remote.api.StaffApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

class GroupRepositoryImpl(
        private val groupDao: GroupDao,
        private val specialityDao: SpecialityDao,
        private val api: StaffApi
) : BaseRepository(), IGroupRepository {

    override suspend fun getAll(forceUpdate: Boolean): Flow<List<Group>> {
        withContext(coroutineContext) {
            launch {
                if (forceUpdate || !isCached()) {
                    updateCache()
                }
            }
        }
        return getGroupsFromDao()
    }

    override suspend fun getById(id: Long): Group {
        val groupDB = performDaoCall { groupDao.getById(id) }
        val faculty = groupDB.facultyId?.let {
            performDaoCall { specialityDao.getFacultyById(it) }
        }
        return groupDB.toDomainEntity(faculty = faculty?.toDomainEntity())
    }

    override suspend fun updateCache() {
        val loadedGroups = loadGroupsFromApi()
        deleteAll()
        storeGroups(loadedGroups)
    }

    override suspend fun deleteAll() {
        performDaoCall { groupDao.deleteAll() }
    }

    override suspend fun isCached(): Boolean = groupDao.isNotEmpty()

    private suspend fun loadGroupsFromApi(): List<Group> =
            performApiCall { api.getAllGroups() }
                    .map { dto ->
                        val faculty = dto.facultyId?.let {
                            performDaoCall { specialityDao.getFacultyById(it) }
                        }
                        dto.toDomainEntity(
                                faculty = faculty?.toDomainEntity()
                        )
                    }

    private suspend fun getGroupsFromDao() = performDaoCall { groupDao.getAll() }
            .map { list ->
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
            performDaoCall { groupDao.insert(this) }
        }
    }
}