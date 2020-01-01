package com.pechuro.bsuirschedule.domain.repository

import com.pechuro.bsuirschedule.domain.entity.Classes
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import io.reactivex.Completable
import io.reactivex.Single

interface IScheduleRepository {

    fun getClasses(name: String, vararg type: ScheduleType): Single<MutableList<Classes>>

    fun update(schedule: Schedule): Completable

    fun delete(name: String, type: ScheduleType): Completable
}
