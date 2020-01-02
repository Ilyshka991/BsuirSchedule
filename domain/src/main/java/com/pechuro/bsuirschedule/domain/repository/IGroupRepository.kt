package com.pechuro.bsuirschedule.domain.repository

import com.pechuro.bsuirschedule.domain.entity.Group
import kotlinx.coroutines.flow.Flow

interface IGroupRepository {

    suspend fun getAll(): Flow<List<Group>>

    suspend fun getByNumber(number: String): Group

    suspend fun update()

    suspend fun deleteAll()

    suspend fun isStored(): Boolean
}
