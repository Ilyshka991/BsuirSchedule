package com.pechuro.bsuirschedule.remote.api

import com.pechuro.bsuirschedule.remote.dto.GroupDTO
import io.reactivex.Single
import retrofit2.http.GET

interface GroupApi {

    @GET("groups")
    fun get(): Single<List<GroupDTO>>
}
