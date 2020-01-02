package com.pechuro.bsuirschedule.domain.repository

import com.pechuro.bsuirschedule.domain.entity.Auditory
import com.pechuro.bsuirschedule.domain.entity.AuditoryType
import kotlinx.coroutines.flow.Flow

interface IBuildingRepository {

    suspend fun getAllAuditories(): Flow<List<Auditory>>

    suspend fun getAllAuditoryTypes(): Flow<List<AuditoryType>>

    suspend fun update()

    suspend fun isStored(): Boolean
}