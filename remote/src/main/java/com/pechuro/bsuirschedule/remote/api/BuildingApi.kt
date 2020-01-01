package com.pechuro.bsuirschedule.remote.api

import com.pechuro.bsuirschedule.remote.dto.AuditoryDTO
import io.reactivex.Single
import retrofit2.http.GET

interface BuildingApi {

    @GET("auditory")
    fun getAllAuditories(): Single<List<AuditoryDTO>>
}
