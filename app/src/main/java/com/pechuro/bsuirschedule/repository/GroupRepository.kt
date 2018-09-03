package com.pechuro.bsuirschedule.repository

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import com.pechuro.bsuirschedule.repository.api.GroupApi
import com.pechuro.bsuirschedule.repository.entity.Group
import com.pechuro.bsuirschedule.repository.db.GroupDao

class GroupRepository(private val groupApi: GroupApi, private val groupDao: GroupDao) {

    fun clearGroups() {
        groupDao.delete()
    }

    fun getGroups(): Observable<List<Group>> {
        return Observable.concatArray(
                getFromDb(),
                getFromApi())
    }


    private fun getFromDb(): Observable<List<Group>> {
        return groupDao.getGroups().filter { it.isNotEmpty() }
                .toObservable()
                .doOnNext {
                }
    }

    private fun getFromApi(): Observable<List<Group>> {
        return groupApi.getGroups()
                .doOnNext {
                    storeInDb(it)
                }
    }

    private fun storeInDb(users: List<Group>) {
        Observable.fromCallable { groupDao.insertAll(users) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe {
                }
    }

}
