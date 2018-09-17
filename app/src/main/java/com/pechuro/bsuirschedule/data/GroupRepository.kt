package com.pechuro.bsuirschedule.data

import com.pechuro.bsuirschedule.data.database.dao.GroupDao
import com.pechuro.bsuirschedule.data.entity.Group
import com.pechuro.bsuirschedule.data.network.GroupApi
import io.reactivex.Single
import javax.inject.Inject

class GroupRepository @Inject constructor(private val api: GroupApi, private val dao: GroupDao) {
    val isCacheNotEmpty get() = dao.isNotEmpty()

    fun getGroups(): Single<List<Group>> = getFromCache().onErrorResumeNext { getFromApi() }

    fun delete() = dao.delete()

    private fun getFromCache(): Single<List<Group>> =
            dao.get().filter { it.isNotEmpty() }.toSingle()

    private fun getFromApi(): Single<List<Group>> =
            api.get().doOnSuccess { storeInCache(it) }

    private fun storeInCache(groups: List<Group>) =
            dao.insert(groups)
}
