package com.pechuro.bsuirschedule.data

import com.pechuro.bsuirschedule.data.database.dao.GroupDao
import com.pechuro.bsuirschedule.data.entity.Group
import com.pechuro.bsuirschedule.data.network.GroupApi
import io.reactivex.Single
import javax.inject.Inject

class GroupRepository @Inject constructor(private val api: GroupApi, private val dao: GroupDao) {
    val isCacheNotEmpty get() = dao.isNotEmpty()

    fun get(): Single<List<Group>> = getFromCache()

    fun load(): Single<Boolean> = getFromApi()

    fun delete() = dao.delete()

    private fun getFromCache(): Single<List<Group>> =
            dao.get().filter { it.isNotEmpty() }.toSingle()

    private fun getFromApi(): Single<Boolean> =
            api.get().doOnSuccess {
                storeInCache(it)
            }.map { true }.onErrorReturn { false }

    private fun storeInCache(groups: List<Group>) =
            dao.insert(groups)
}
