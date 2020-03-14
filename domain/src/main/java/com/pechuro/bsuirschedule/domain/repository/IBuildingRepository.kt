package com.pechuro.bsuirschedule.domain.repository

import com.pechuro.bsuirschedule.domain.entity.Auditory
import com.pechuro.bsuirschedule.domain.entity.AuditoryType
import kotlinx.coroutines.flow.Flow

interface IBuildingRepository {

    suspend fun getAllAuditories(forceUpdate: Boolean = false): Flow<List<Auditory>>

    suspend fun getAllAuditoryTypes(): Flow<List<AuditoryType>>

    suspend fun getAuditoryById(id: Long): Auditory

    suspend fun updateCache()

    suspend fun isCached(): Boolean
}