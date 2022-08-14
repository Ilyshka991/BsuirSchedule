package com.pechuro.bsuirschedule.remote.api

import com.pechuro.bsuirschedule.remote.dto.AuditoryDTO
import retrofit2.http.GET

interface BuildingApi {

    @GET("auditories")
    suspend fun getAllAuditories(): List<AuditoryDTO>
}
