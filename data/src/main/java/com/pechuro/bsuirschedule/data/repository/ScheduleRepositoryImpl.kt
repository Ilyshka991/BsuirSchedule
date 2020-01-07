package com.pechuro.bsuirschedule.data.repository

import com.pechuro.bsuirschedule.data.common.BaseRepository
import com.pechuro.bsuirschedule.domain.entity.Classes
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import com.pechuro.bsuirschedule.domain.repository.IScheduleRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ScheduleRepositoryImpl : BaseRepository(), IScheduleRepository {

    override suspend fun getAllCachedSchedules(): Flow<List<Schedule>> {
        return flow {
            emit(listOf(Schedule(3, "7535", ScheduleType.EMPLOYEE_EXAMS, null),
                    Schedule(4, "235", ScheduleType.EMPLOYEE_EXAMS, null),
                    Schedule(5, "75666666635", ScheduleType.EMPLOYEE_EXAMS, null),
                    Schedule(6, "36", ScheduleType.EMPLOYEE_CLASSES, null)))
            delay(4000)
            emit(listOf(Schedule(7, "666", ScheduleType.EMPLOYEE_EXAMS, null),
                    Schedule(8, "75365", ScheduleType.EMPLOYEE_EXAMS, null),
                    Schedule(9, "753345", ScheduleType.EMPLOYEE_EXAMS, null),
                    Schedule(0, "364636", ScheduleType.EMPLOYEE_CLASSES, null)))
        }
    }

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