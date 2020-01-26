package com.pechuro.bsuirschedule.data.repository

import com.pechuro.bsuirschedule.data.common.BaseRepository
import com.pechuro.bsuirschedule.data.mappers.toDatabaseEntity
import com.pechuro.bsuirschedule.data.mappers.toDomainEntity
import com.pechuro.bsuirschedule.domain.entity.Group
import com.pechuro.bsuirschedule.domain.repository.IGroupRepository
import com.pechuro.bsuirschedule.domain.repository.ISpecialityRepository
import com.pechuro.bsuirschedule.local.dao.GroupDao
import com.pechuro.bsuirschedule.remote.api.StaffApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

class GroupRepositoryImpl(
        private val dao: GroupDao,
        private val api: StaffApi,
        private val specialityRepository: ISpecialityRepository
) : BaseRepository(), IGroupRepository {

    override suspend fun getAll(): Flow<List<Group>> {
        withContext(coroutineContext) {
            if (!isCached()) {
                updateCache()
            }
        }
        return getGroupsFromDao()
    }

    override suspend fun getAllNumbers(): Flow<List<String>> {
        return performDaoCall { dao.getAllNumbers() }
    }

    override suspend fun getById(id: Long): Group {
        val groupCached = performDaoCall { dao.getById(id) }
        val faculty = groupCached.facultyId?.let { specialityRepository.getFacultyById(it) }
        return groupCached.toDomainEntity(faculty = faculty)
    }

    override suspend fun updateCache() {
        val loadedGroups = loadGroupsFromApi()
        storeGroups(loadedGroups)
    }

    override suspend fun deleteAll() {
        performDaoCall { dao.deleteAll() }
    }

    override suspend fun isCached(): Boolean = dao.isNotEmpty()

    private suspend fun loadGroupsFromApi(): List<Group> =
            performApiCall { api.getAllGroups() }
                    .map { dto ->
                        val faculty = dto.facultyId?.let { specialityRepository.getFacultyById(it) }
                        dto.toDomainEntity(
                                faculty = faculty
                        )
                    }

    private suspend fun getGroupsFromDao() = performDaoCall { dao.getAll() }
            .map { cachedList ->
                cachedList.map { groupCached ->
                    val faculty = groupCached.facultyId?.let { specialityRepository.getFacultyById(it) }
                    groupCached.toDomainEntity(
                            faculty = faculty
                    )
                }
            }

    private suspend fun storeGroups(groups: List<Group>) {
        groups.map {
            it.toDatabaseEntity()
        }.run {
            performDaoCall { dao.insert(this) }
        }
    }
}