package com.pechuro.bsuirschedule.remote.api

import com.pechuro.bsuirschedule.remote.dto.AuditoryDTO
import retrofit2.http.GET

interface BuildingApi {

    @GET("auditory")
    suspend  fun getAllAuditories(): List<AuditoryDTO>
}
