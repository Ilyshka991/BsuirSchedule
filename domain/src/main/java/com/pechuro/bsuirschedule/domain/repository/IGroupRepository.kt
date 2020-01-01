package com.pechuro.bsuirschedule.domain.repository

import com.pechuro.bsuirschedule.domain.entity.Group
import io.reactivex.Completable
import io.reactivex.Single

interface IGroupRepository {

    fun getAll(): Single<List<Group>>

    fun deleteAll(): Completable

    fun isStored(): Single<Boolean>
}
