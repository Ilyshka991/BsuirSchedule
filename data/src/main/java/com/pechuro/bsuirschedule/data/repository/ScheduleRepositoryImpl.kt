package com.pechuro.bsuirschedule.data.repository

import com.pechuro.bsuirschedule.data.common.BaseRepository
import com.pechuro.bsuirschedule.data.mappers.*
import com.pechuro.bsuirschedule.domain.entity.*
import com.pechuro.bsuirschedule.domain.repository.IBuildingRepository
import com.pechuro.bsuirschedule.domain.repository.IGroupRepository
import com.pechuro.bsuirschedule.domain.repository.IScheduleRepository
import com.pechuro.bsuirschedule.domain.repository.ISpecialityRepository
import com.pechuro.bsuirschedule.local.dao.ScheduleDao
import com.pechuro.bsuirschedule.remote.api.ScheduleApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.util.*

class ScheduleRepositoryImpl(
        private val dao: ScheduleDao,
        private val api: ScheduleApi,
        private val groupRepository: IGroupRepository,
        private val buildingRepository: IBuildingRepository,
        private val specialityRepository: ISpecialityRepository
) : BaseRepository(), IScheduleRepository {

    override suspend fun getAllSchedules(): Flow<List<Schedule>> {
        return flow {}
    }

    override suspend fun <T : Schedule> getScheduleItems(schedule: T): Flow<ScheduleItem<T>> {
        TODO("not implemented")
    }

    override suspend fun loadGroupSchedule(group: Group, types: List<ScheduleType>) {
        val auditories = buildingRepository.getAllAuditories().first()
        val departments = specialityRepository.getAllDepartments().first()

        val scheduleDTO = performApiCall { api.getStudentSchedule(group.id) }
        val lastUpdatedDate: Date = performApiCallCatching(Date(0)) {
            api.getLastUpdateDate(group.number).toDomainEntity()
        }

        types.forEach { type ->
            when (type) {
                ScheduleType.CLASSES -> {
                    val itemsDTOList = scheduleDTO.schedule ?: emptyList()
                    val schedule = Schedule.GroupClasses(
                            name = group.number,
                            lastUpdated = lastUpdatedDate,
                            group = group
                    )
                    val items = itemsDTOList.toGroupLessons(schedule, auditories, departments)

                    storeSchedule(schedule, items)
                }
                ScheduleType.EXAMS -> {
                    val itemsDTOList = scheduleDTO.exam ?: emptyList()
                    val schedule = Schedule.GroupExams(
                            name = group.number,
                            lastUpdated = lastUpdatedDate,
                            group = group
                    )
                    val items = itemsDTOList.toGroupExams(schedule, auditories, departments)

                    storeSchedule(schedule, items)
                }
            }
        }
    }

    override suspend fun loadEmployeeSchedule(employee: Employee, types: List<ScheduleType>) {
        val groups = groupRepository.getAll().first()
        val auditories = buildingRepository.getAllAuditories().first()

        val scheduleDTO = performApiCall { api.getEmployeeSchedule(employee.id) }

        types.forEach { type ->
            when (type) {
                ScheduleType.CLASSES -> {
                    val itemsDTOList = scheduleDTO.schedule ?: emptyList()
                    val schedule = Schedule.EmployeeClasses(
                            name = employee.abbreviation,
                            employee = employee
                    )
                    val items = itemsDTOList.toEmployeeLessons(schedule, groups, auditories)

                    storeSchedule(schedule, items)
                }
                ScheduleType.EXAMS -> {
                    val itemsDTOList = scheduleDTO.exam ?: emptyList()
                    val schedule = Schedule.EmployeeExams(
                            name = employee.abbreviation,
                            employee = employee
                    )
                    val items = itemsDTOList.toEmployeeExams(schedule, groups, auditories)

                    storeSchedule(schedule, items)
                }
            }
        }
    }

    override suspend fun updateAll() {
        TODO("not implemented")
    }

    override suspend fun update(schedule: Schedule) {
        TODO("not implemented")
    }

    override suspend fun isUpdateAvailable(schedule: Schedule): Boolean {
        TODO("not implemented")
    }

    override suspend fun delete(schedule: Schedule) {
        TODO("not implemented")
    }

    override suspend fun deleteAll() {
        TODO("not implemented")
    }

    override suspend fun getCurrentWeek(): Int {
        TODO("not implemented")
    }

    private suspend fun storeSchedule(schedule: Schedule.GroupClasses, items: List<ScheduleItem.GroupLesson>) {
        val cachedSchedule = schedule.toDatabaseEntity()
        val cachedItems = items.map { it.toDatabaseEntity(cachedSchedule) }
        performDaoCall {
            dao.insertGroupClassesSchedule(cachedSchedule, cachedItems)
        }
    }

    private suspend fun storeSchedule(schedule: Schedule.GroupExams, items: List<ScheduleItem.GroupExam>) {
        val cachedSchedule = schedule.toDatabaseEntity()
        val cachedItems = items.map { it.toDatabaseEntity(cachedSchedule) }
        performDaoCall {
            dao.insertGroupExamsSchedule(cachedSchedule, cachedItems)
        }
    }

    private suspend fun storeSchedule(schedule: Schedule.EmployeeClasses, items: List<ScheduleItem.EmployeeLesson>) {
        val cachedSchedule = schedule.toDatabaseEntity()
        val cachedItems = items.map { it.toDatabaseEntity(cachedSchedule) }
        performDaoCall {
            dao.insertEmployeeClassesSchedule(cachedSchedule, cachedItems)
        }
    }

    private suspend fun storeSchedule(schedule: Schedule.EmployeeExams, items: List<ScheduleItem.EmployeeExam>) {
        val cachedSchedule = schedule.toDatabaseEntity()
        val cachedItems = items.map { it.toDatabaseEntity(cachedSchedule) }
        performDaoCall {
            dao.insertEmployeeExamsSchedule(cachedSchedule, cachedItems)
        }
    }
}