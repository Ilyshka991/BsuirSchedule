package com.pechuro.bsuirschedule.data.repository

import com.pechuro.bsuirschedule.domain.entity.Group
import com.pechuro.bsuirschedule.domain.repository.IGroupRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class GroupRepositoryImpl : IGroupRepository {

    override fun getAll(): Observable<List<Group>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getByNumber(number: String): Single<Group> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAll(): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isStored(): Single<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}