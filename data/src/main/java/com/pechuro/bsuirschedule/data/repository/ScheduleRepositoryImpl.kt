package com.pechuro.bsuirschedule.data.repository

import com.pechuro.bsuirschedule.domain.entity.Classes
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import com.pechuro.bsuirschedule.domain.repository.IScheduleRepository
import io.reactivex.Completable
import io.reactivex.Single

class ScheduleRepositoryImpl : IScheduleRepository {

    override fun getClasses(name: String, vararg type: ScheduleType): Single<MutableList<Classes>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(schedule: Schedule): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(name: String, type: ScheduleType): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}