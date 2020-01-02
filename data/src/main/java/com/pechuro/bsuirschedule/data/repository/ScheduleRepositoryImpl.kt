package com.pechuro.bsuirschedule.data.repository

import com.pechuro.bsuirschedule.domain.entity.Classes
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import com.pechuro.bsuirschedule.domain.repository.IScheduleRepository
import kotlinx.coroutines.flow.Flow

class ScheduleRepositoryImpl : IScheduleRepository {
    override suspend fun getCurrentWeek(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getAllCachedClasses(): Flow<List<Classes>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getClasses(name: String, vararg type: ScheduleType): List<Classes> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun updateCache(schedule: Schedule) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun updateCached() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun isUpdateAvailable(schedule: Schedule): Flow<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun delete(name: String, type: ScheduleType) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteByType(vararg type: ScheduleType) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteAll() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}