package com.pechuro.bsuirschedule.data.repository

import com.pechuro.bsuirschedule.domain.entity.Classes
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import com.pechuro.bsuirschedule.domain.repository.IScheduleRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class ScheduleRepositoryImpl : IScheduleRepository {

    override fun getCurrentWeek(): Single<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllStoredClasses(): Observable<List<Classes>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getClasses(name: String, vararg type: ScheduleType): Single<List<Classes>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(schedule: Schedule): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateAll(): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isUpdateAvailable(schedule: Schedule): Observable<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(name: String, type: ScheduleType): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteByType(vararg type: ScheduleType): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAll(): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}