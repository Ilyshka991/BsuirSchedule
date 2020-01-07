package com.pechuro.bsuirschedule.domain.repository

import com.pechuro.bsuirschedule.domain.entity.Group
import kotlinx.coroutines.flow.Flow

interface IGroupRepository {

    suspend fun getAll(forceUpdate: Boolean = false): Flow<List<Group>>

    suspend fun getById(id: Long): Group

    suspend fun deleteAll()

    suspend fun isCached(): Boolean

    suspend fun updateCache()
}
