package com.pechuro.bsuirschedule.data

import com.pechuro.bsuirschedule.data.database.dao.GroupDao
import com.pechuro.bsuirschedule.data.entity.Group
import com.pechuro.bsuirschedule.data.network.GroupApi
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class GroupRepository @Inject constructor(private val api: GroupApi, private val dao: GroupDao) {
    fun isCacheNotEmpty() = dao.isNotEmpty()

    fun get(): Single<List<Group>> = dao.get()
            .filter { it.isNotEmpty() }.toSingle()

    fun getGroupNumbers(): Single<List<String>> = dao.getNumbers()

    fun load(): Single<List<Group>> = api.get()
            .doOnSuccess { storeInCache(it).blockingAwait() }

    fun delete() = Completable.fromAction { dao.delete() }

    private fun storeInCache(groups: List<Group>) =
            Completable.fromAction { dao.insert(groups) }
}
