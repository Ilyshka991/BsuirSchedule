package com.pechuro.bsuirschedule.data.repository

import com.pechuro.bsuirschedule.data.common.BaseRepository
import com.pechuro.bsuirschedule.data.mappers.toDatabaseEntity
import com.pechuro.bsuirschedule.data.mappers.toDomainEntity
import com.pechuro.bsuirschedule.domain.entity.Classes
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import com.pechuro.bsuirschedule.domain.repository.IBuildingRepository
import com.pechuro.bsuirschedule.domain.repository.IEmployeeRepository
import com.pechuro.bsuirschedule.domain.repository.IGroupRepository
import com.pechuro.bsuirschedule.domain.repository.IScheduleRepository
import com.pechuro.bsuirschedule.local.dao.ScheduleDao
import com.pechuro.bsuirschedule.remote.api.ScheduleApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class ScheduleRepositoryImpl(
        private val scheduleDao: ScheduleDao,
        private val scheduleApi: ScheduleApi,
        private val employeeRepository: IEmployeeRepository,
        private val groupRepository: IGroupRepository,
        private val buildingRepository: IBuildingRepository
) : BaseRepository(), IScheduleRepository {

    override suspend fun getAllSchedules(): Flow<List<Schedule>> {
        return flow {}
    }

    override suspend fun getCurrentWeek(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getClasses(name: String, type: ScheduleType): Flow<Classes> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getAllClasses(): Flow<List<Classes>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun loadClasses(name: String, types: List<ScheduleType>) {
        val classesList = loadClassesFromApi(name, types)
        storeClasses(classesList)
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

    private suspend fun loadClassesFromApi(name: String, types: List<ScheduleType>): List<Classes> {
        // Check type for first element, because it's not possible to load STUDENT_CLASSES and EMPLOYEE_CLASSES with same name
        val scheduleDTO = when (types[0]) {
            ScheduleType.STUDENT_CLASSES, ScheduleType.STUDENT_EXAMS -> performApiCall {
                scheduleApi.getStudentSchedule(name)
            }
            ScheduleType.EMPLOYEE_CLASSES, ScheduleType.EMPLOYEE_EXAMS -> {
                val employeeId = employeeRepository.getIdByName(name)
                performApiCall { scheduleApi.getEmployeeSchedule(employeeId) }
            }
        }
        val lastUpdatedDate: String? = performApiCallCatching(null) {
            scheduleApi.getLastUpdateDate(name).lastUpdateDate
        }
        val groups = groupRepository.getAll().first()
        val auditories = buildingRepository.getAllAuditories().first()
        return types.map {
            scheduleDTO.toDomainEntity(
                    scheduleType = it,
                    lastUpdated = lastUpdatedDate,
                    groups = groups,
                    auditories = auditories
            )
        }
    }

    private suspend fun storeClasses(classesList: List<Classes>) {
        classesList.map {
            it.toDatabaseEntity()
        }.forEach {
            performDaoCall { scheduleDao.insert(it) }
        }
    }
}