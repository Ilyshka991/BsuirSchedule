package com.pechuro.bsuirschedule.data.repository

import com.pechuro.bsuirschedule.data.common.BaseRepository
import com.pechuro.bsuirschedule.data.mappers.toDatabaseEntity
import com.pechuro.bsuirschedule.data.mappers.toEmployeeScheduleItems
import com.pechuro.bsuirschedule.data.mappers.toGroupScheduleItems
import com.pechuro.bsuirschedule.domain.entity.*
import com.pechuro.bsuirschedule.domain.repository.IBuildingRepository
import com.pechuro.bsuirschedule.domain.repository.IGroupRepository
import com.pechuro.bsuirschedule.domain.repository.IScheduleRepository
import com.pechuro.bsuirschedule.local.dao.ScheduleDao
import com.pechuro.bsuirschedule.remote.api.ScheduleApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class ScheduleRepositoryImpl(
        private val dao: ScheduleDao,
        private val api: ScheduleApi,
        private val groupRepository: IGroupRepository,
        private val buildingRepository: IBuildingRepository
) : BaseRepository(), IScheduleRepository {

    override suspend fun getAllSchedules(): Flow<List<Schedule>> {
        return flow {}
    }

    override suspend fun getScheduleItems(schedule: Schedule): Flow<ScheduleItem> {
        TODO("not implemented")
    }

    override suspend fun loadGroupSchedule(group: Group, types: List<ScheduleType>) {
        val schedules = loadGroupScheduleFromApi(group, types)
        schedules.forEach { (schedule, items) ->
            storeSchedule(schedule, items)
        }
    }

    override suspend fun loadEmployeeSchedule(employee: Employee, types: List<ScheduleType>) {
        val schedules = loadEmployeeScheduleFromApi(employee, types)
        schedules.forEach { (schedule, items) ->
            storeSchedule(schedule, items)
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

    private suspend fun loadGroupScheduleFromApi(
            group: Group,
            types: List<ScheduleType>
    ): List<Pair<Schedule.GroupSchedule, List<ScheduleItem.GroupScheduleItem>>> {
        val auditories = buildingRepository.getAllAuditories().first()

        val scheduleDTO = performApiCall { api.getStudentSchedule(group.id) }
        val lastUpdatedDate: String? = performApiCallCatching(null) {
            api.getLastUpdateDate(group.number).lastUpdateDate
        }

        return types.map { type ->
            val itemsDTOList = when (type) {
                ScheduleType.CLASSES -> scheduleDTO.schedule
                ScheduleType.EXAMS -> scheduleDTO.exam
            } ?: emptyList()

            val schedule = Schedule.GroupSchedule(
                    name = group.number,
                    type = type,
                    lastUpdated = lastUpdatedDate,
                    group = group
            )
            val items = itemsDTOList.toGroupScheduleItems(schedule, auditories)

            schedule to items
        }

    }

    private suspend fun loadEmployeeScheduleFromApi(
            employee: Employee,
            types: List<ScheduleType>
    ): List<Pair<Schedule.EmployeeSchedule, List<ScheduleItem.EmployeeScheduleItem>>> {
        val groups = groupRepository.getAll().first()
        val auditories = buildingRepository.getAllAuditories().first()

        val scheduleDTO = performApiCall { api.getEmployeeSchedule(employee.id) }

        return types.map { type ->
            val itemsDTOList = when (type) {
                ScheduleType.CLASSES -> scheduleDTO.schedule
                ScheduleType.EXAMS -> scheduleDTO.exam
            } ?: emptyList()

            val schedule = Schedule.EmployeeSchedule(
                    name = employee.abbreviation,
                    type = type,
                    employee = employee
            )
            val items = itemsDTOList.toEmployeeScheduleItems(schedule, groups, auditories)

            schedule to items
        }
    }

    private suspend fun storeSchedule(
            schedule: Schedule.GroupSchedule,
            items: List<ScheduleItem.GroupScheduleItem>
    ) {
        val cachedSchedule = schedule.toDatabaseEntity()
        val cachedItems = items.map { it.toDatabaseEntity(cachedSchedule) }
        performDaoCall {
            dao.insertGroupSchedule(cachedSchedule, cachedItems)
        }
    }

    private suspend fun storeSchedule(
            schedule: Schedule.EmployeeSchedule,
            items: List<ScheduleItem.EmployeeScheduleItem>
    ) {
        val cachedSchedule = schedule.toDatabaseEntity()
        val cachedItems = items.map { it.toDatabaseEntity(cachedSchedule) }
        performDaoCall {
            dao.insertEmployeeSchedule(cachedSchedule, cachedItems)
        }
    }
}