package com.pechuro.bsuirschedule.repository

import com.pechuro.bsuirschedule.repository.api.GroupApi
import com.pechuro.bsuirschedule.repository.db.dao.GroupDao
import com.pechuro.bsuirschedule.repository.entity.Group
import io.reactivex.Single

class GroupRepository(private val api: GroupApi, private val dao: GroupDao) {
    val isCacheNotEmpty get() = dao.isNotEmpty()

    fun getGroups(): Single<List<Group>> = getFromCache().onErrorResumeNext(getFromApi())

    fun delete() = dao.delete()

    private fun getFromCache(): Single<List<Group>> =
            dao.get().filter { it.isNotEmpty() }.toSingle()

    private fun getFromApi(): Single<List<Group>> =
            api.get().doOnSuccess { storeInCache(it) }

    private fun storeInCache(groups: List<Group>) =
            dao.insert(groups)
}
