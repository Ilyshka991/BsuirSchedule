package com.pechuro.bsuirschedule.domain.repository

import com.pechuro.bsuirschedule.domain.entity.Classes
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import io.reactivex.Completable
import io.reactivex.Single

interface IScheduleRepository {

    fun getCurrentWeek(): Single<Int>

    fun getAllStoredClasses(): Single<List<Classes>>

    fun getClasses(name: String, vararg type: ScheduleType): Single<List<Classes>>

    fun update(schedule: Schedule): Completable

    fun updateAll(): Completable

    fun isUpdateAvailable(schedule: Schedule): Single<Boolean>

    fun delete(name: String, type: ScheduleType): Completable

    fun deleteByType(vararg type: ScheduleType): Completable

    fun deleteAll(): Completable
}
