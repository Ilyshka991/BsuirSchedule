package com.pechuro.bsuirschedule.repository.api

import io.reactivex.Observable
import com.pechuro.bsuirschedule.repository.entity.Group
import retrofit2.http.GET

interface GroupApi {

    @GET("v1/groups")
    fun getGroups(): Observable<List<Group>>
}
