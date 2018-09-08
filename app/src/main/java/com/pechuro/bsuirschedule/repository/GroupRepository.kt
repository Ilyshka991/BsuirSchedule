package com.pechuro.bsuirschedule.repository

import com.pechuro.bsuirschedule.repository.api.GroupApi
import com.pechuro.bsuirschedule.repository.db.dao.GroupDao
import com.pechuro.bsuirschedule.repository.entity.Group
import io.reactivex.Single
import org.jetbrains.anko.doAsync

class GroupRepository(private val api: GroupApi, private val dao: GroupDao) {

    fun getGroups(): Single<List<Group>> =
            getFromDb().onErrorResumeNext(getFromApi())


    private fun getFromDb(): Single<List<Group>> =
            dao.getGroups().filter { it.isNotEmpty() }.toSingle()

    private fun getFromApi(): Single<List<Group>> =
            api.getGroups().doOnSuccess { storeInDb(it) }

    private fun storeInDb(groups: List<Group>) =
            doAsync { dao.insert(groups) }
}
