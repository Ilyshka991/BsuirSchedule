package com.pechuro.bsuirschedule.domain.repository

import com.pechuro.bsuirschedule.domain.entity.Group
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface IGroupRepository {

    fun getAll(): Observable<List<Group>>

    fun getByNumber(number: String): Single<Group>

    fun update(): Completable

    fun deleteAll(): Completable

    fun isStored(): Single<Boolean>
}
