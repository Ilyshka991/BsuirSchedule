package com.pechuro.bsuirschedule.data.repository

import com.pechuro.bsuirschedule.data.common.BaseRepository
import com.pechuro.bsuirschedule.data.mappers.toDatabaseEntity
import com.pechuro.bsuirschedule.data.mappers.toDomainEntity
import com.pechuro.bsuirschedule.domain.entity.Group
import com.pechuro.bsuirschedule.domain.repository.IGroupRepository
import com.pechuro.bsuirschedule.domain.repository.ISpecialityRepository
import com.pechuro.bsuirschedule.local.dao.GroupDao
import com.pechuro.bsuirschedule.remote.api.StaffApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class GroupRepositoryImpl(
        private val dao: GroupDao,
        private val api: StaffApi,
        private val specialityRepository: ISpecialityRepository
) : BaseRepository(), IGroupRepository {

    override suspend fun getAll(): Flow<List<Group>> {
        if (!isCached()) {
            updateCache()
        }
        return getGroupsFromDao()
    }

    override suspend fun getById(id: Long): Group {
        val groupCached = performDaoCall { dao.getById(id) }
        val speciality = specialityRepository.getSpecialityById(groupCached.specialityId)
        return groupCached.toDomainEntity(
                speciality = speciality
        )
    }

    override suspend fun updateCache() {
        val loadedGroups = loadGroupsFromApi()
        storeGroups(loadedGroups)
    }

    override suspend fun isCached(): Boolean = dao.isNotEmpty()

    private suspend fun loadGroupsFromApi(): List<Group> =
            performApiCall { api.getAllGroups() }
                    .map { dto ->
                        val speciality = specialityRepository.getSpecialityById(dto.specialityId)
                        dto.toDomainEntity(
                                speciality = speciality
                        )
                    }

    private suspend fun getGroupsFromDao() = dao.getAll()
            .map { cachedList ->
                val allSpecialities = specialityRepository.getAllSpecialities().first()
                cachedList.mapNotNull { groupCached ->
                    val speciality = allSpecialities.find { it.id == groupCached.specialityId }
                            ?: return@mapNotNull null
                    groupCached.toDomainEntity(
                            speciality = speciality
                    )
                }
            }
            .flowOn(Dispatchers.IO)

    private suspend fun storeGroups(groups: List<Group>) {
        groups.map {
            it.toDatabaseEntity()
        }.run {
            performDaoCall { dao.insertOrUpdate(this) }
        }
    }
}