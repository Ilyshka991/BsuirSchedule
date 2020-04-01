package com.pechuro.bsuirschedule.data.repository

import com.pechuro.bsuirschedule.data.common.BaseRepository
import com.pechuro.bsuirschedule.data.mappers.*
import com.pechuro.bsuirschedule.domain.entity.*
import com.pechuro.bsuirschedule.domain.repository.*
import com.pechuro.bsuirschedule.local.dao.ScheduleDao
import com.pechuro.bsuirschedule.remote.api.ScheduleApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.coroutines.coroutineContext
import kotlin.reflect.KClass

class ScheduleRepositoryImpl(
        private val dao: ScheduleDao,
        private val api: ScheduleApi,
        private val groupRepository: IGroupRepository,
        private val employeeRepository: IEmployeeRepository,
        private val buildingRepository: IBuildingRepository,
        private val specialityRepository: ISpecialityRepository
) : BaseRepository(), IScheduleRepository {

    override suspend fun getAllSchedules(): Flow<List<Schedule>> = flowOf(
            performDaoCall {
                dao.getAllGroupClassesSchedules().map { list ->
                    Schedule.GroupClasses::class to list.map { item ->
                        val group = groupRepository.getById(item.groupId)
                        item.toDomainEntity(group)
                    }
                }
            },
            performDaoCall {
                dao.getAllGroupExamSchedules().map { list ->
                    Schedule.GroupExams::class to list.map { item ->
                        val group = groupRepository.getById(item.groupId)
                        item.toDomainEntity(group)
                    }
                }
            },
            performDaoCall {
                dao.getAllEmployeeClassesSchedules().map { list ->
                    Schedule.EmployeeClasses::class to list.map { item ->
                        val employee = employeeRepository.getById(item.employeeId)
                        item.toDomainEntity(employee)
                    }
                }
            },
            performDaoCall {
                dao.getAllEmployeeExamSchedules().map { list ->
                    Schedule.EmployeeExams::class to list.map { item ->
                        val employee = employeeRepository.getById(item.employeeId)
                        item.toDomainEntity(employee)
                    }
                }
            }
    )
            .flattenMerge(4)
            .scan(emptyMap<KClass<out Schedule>, List<Schedule>>()) { acc, value ->
                val resultMap = acc.toMutableMap()
                resultMap[value.first] = value.second
                resultMap
            }
            .map { map -> map.flatMap { it.value } }
            .map {
                it.asReversed()
            }
            .flowOn(Dispatchers.IO)

    override suspend fun getGroupClassesByName(name: String): Schedule.GroupClasses {
        val schedule = performDaoCall { dao.getGroupClassesByName(name) }
        val group = groupRepository.getById(schedule.groupId)
        return schedule.toDomainEntity(group)
    }

    override suspend fun getGroupExamsByName(name: String): Schedule.GroupExams {
        val schedule = performDaoCall { dao.getGroupExamsByName(name) }
        val group = groupRepository.getById(schedule.groupId)
        return schedule.toDomainEntity(group)
    }

    override suspend fun getEmployeeClassesByName(name: String): Schedule.EmployeeClasses {
        val schedule = performDaoCall { dao.getEmployeeClassesByName(name) }
        val employee = employeeRepository.getById(schedule.employeeId)
        return schedule.toDomainEntity(employee)
    }

    override suspend fun getEmployeeExamsByName(name: String): Schedule.EmployeeExams {
        val schedule = performDaoCall { dao.getEmployeeExamsByName(name) }
        val employee = employeeRepository.getById(schedule.employeeId)
        return schedule.toDomainEntity(employee)
    }

    override suspend fun getScheduleItems(schedule: Schedule): Flow<List<ScheduleItem>> {
        return when (schedule) {
            is Schedule.GroupClasses -> dao.getGroupClassesItems(schedule.name).map { list ->
                list.map { item ->
                    val auditories = item.auditories.map { buildingRepository.getAuditoryById(it.id) }
                    val employees = item.employees.map { employeeRepository.getById(it.id) }
                    item.toDomainEntity(
                            auditories = auditories,
                            employees = employees
                    )
                }
            }
            is Schedule.GroupExams -> dao.getGroupExamItems(schedule.name).map { list ->
                list.map { item ->
                    val auditories = item.auditories.map { buildingRepository.getAuditoryById(it.id) }
                    val employees = item.employees.map { employeeRepository.getById(it.id) }
                    item.toDomainEntity(
                            auditories = auditories,
                            employees = employees
                    )
                }
            }
            is Schedule.EmployeeClasses -> dao.getEmployeeClassesItems(schedule.name).map { list ->
                list.map { item ->
                    val auditories = item.auditories.map { buildingRepository.getAuditoryById(it.id) }
                    val groups = item.groups.map { groupRepository.getById(it.id) }
                    item.toDomainEntity(
                            auditories = auditories,
                            groups = groups
                    )
                }
            }
            is Schedule.EmployeeExams -> dao.getEmployeeExamItems(schedule.name).map { list ->
                list.map { item ->
                    val auditories = item.auditories.map { buildingRepository.getAuditoryById(it.id) }
                    val groups = item.groups.map { groupRepository.getById(it.id) }
                    item.toDomainEntity(
                            auditories = auditories,
                            groups = groups
                    )
                }
            }
        }
    }

    override suspend fun loadGroupSchedule(group: Group, types: List<ScheduleType>): List<Schedule> {
        return loadGroupScheduleInternal(
                group = group,
                types = types,
                update = false
        )
    }

    override suspend fun loadEmployeeSchedule(employee: Employee, types: List<ScheduleType>): List<Schedule> {
        return loadEmployeeScheduleInternal(
                employee = employee,
                types = types,
                update = false
        )
    }

    override suspend fun updateAll() {
        getAllSchedules().first().forEach {
            update(it)
        }
    }

    override suspend fun update(schedule: Schedule) {
        when (schedule) {
            is Schedule.GroupClasses -> loadGroupScheduleInternal(
                    group = schedule.group,
                    types = listOf(ScheduleType.CLASSES),
                    update = true
            )
            is Schedule.GroupExams -> loadGroupScheduleInternal(
                    group = schedule.group,
                    types = listOf(ScheduleType.EXAMS),
                    update = true
            )
            is Schedule.EmployeeClasses -> loadEmployeeScheduleInternal(
                    employee = schedule.employee,
                    types = listOf(ScheduleType.CLASSES),
                    update = true
            )
            is Schedule.EmployeeExams -> loadEmployeeScheduleInternal(
                    employee = schedule.employee,
                    types = listOf(ScheduleType.EXAMS),
                    update = true
            )
        }
    }

    override suspend fun setNotRemindForUpdates(schedule: Schedule, notRemind: Boolean) {
        when (schedule) {
            is Schedule.GroupClasses -> {
                val newSchedule = Schedule.GroupClasses(
                        name = schedule.name,
                        group = schedule.group,
                        lastUpdatedDate = schedule.lastUpdatedDate,
                        notRemindForUpdates = notRemind
                ).toDatabaseEntity()
                performDaoCall { dao.update(newSchedule) }
            }
            is Schedule.GroupExams -> {
                val newSchedule = Schedule.GroupExams(
                        name = schedule.name,
                        group = schedule.group,
                        lastUpdatedDate = schedule.lastUpdatedDate,
                        notRemindForUpdates = notRemind
                ).toDatabaseEntity()
                performDaoCall { dao.update(newSchedule) }
            }
        }
    }

    override suspend fun isUpdateAvailable(schedule: Schedule) = when (schedule) {
        is Schedule.GroupClasses -> {
            val newLastUpdateDate = performApiCall { api.getLastUpdateDate(schedule.group.number) }.toDomainEntity()
            schedule.lastUpdatedDate < newLastUpdateDate
        }
        is Schedule.GroupExams -> {
            val newLastUpdate = performApiCall { api.getLastUpdateDate(schedule.group.number) }.toDomainEntity()
            schedule.lastUpdatedDate < newLastUpdate
        }
        else -> false
    }

    override suspend fun deleteSchedule(schedule: Schedule) {
        performDaoCall {
            when (schedule) {
                is Schedule.GroupClasses -> dao.deleteGroupClassesSchedule(schedule.name)
                is Schedule.GroupExams -> dao.deleteGroupExamSchedule(schedule.name)
                is Schedule.EmployeeClasses -> dao.deleteEmployeeClassesSchedule(schedule.name)
                is Schedule.EmployeeExams -> dao.deleteEmployeeExamSchedule(schedule.name)
            }
        }
    }

    override suspend fun deleteScheduleItem(scheduleItem: ScheduleItem) {
        performDaoCall {
            val id = scheduleItem.id
            when (scheduleItem) {
                is Lesson.GroupLesson -> dao.deleteGroupClassesItem(id)
                is Lesson.EmployeeLesson -> dao.deleteEmployeeClassesItem(id)
                is Exam.GroupExam -> dao.deleteGroupExamItem(id)
                is Exam.EmployeeExam -> dao.deleteEmployeeExamItem(id)
            }
        }
    }

    override suspend fun deleteAllSchedules() {
        withContext(coroutineContext) {
            listOf(
                    async { performDaoCall { dao.deleteAllGroupClassesSchedules() } },
                    async { performDaoCall { dao.deleteAllGroupExamSchedules() } },
                    async { performDaoCall { dao.deleteAllEmployeeClassesSchedules() } },
                    async { performDaoCall { dao.deleteAllEmployeeExamSchedules() } }
            ).awaitAll()
        }
    }

    private suspend fun loadGroupScheduleInternal(
            group: Group,
            types: List<ScheduleType>,
            update: Boolean
    ): List<Schedule> {
        val auditories = buildingRepository.getAllAuditories().first()
        val departments = specialityRepository.getAllDepartments().first()

        val scheduleDTO = performApiCall { api.getStudentSchedule(group.id) }
        val lastUpdatedDate: Date = performApiCallCatching(Date(0)) {
            api.getLastUpdateDate(group.number).toDomainEntity()
        }

        return types.map { type ->
            when (type) {
                ScheduleType.CLASSES -> {
                    val itemsDTOList = scheduleDTO.schedule ?: emptyList()
                    val schedule = Schedule.GroupClasses(
                            name = group.number,
                            lastUpdatedDate = lastUpdatedDate,
                            group = group,
                            notRemindForUpdates = false
                    )
                    val items = itemsDTOList.toGroupLessons(auditories, departments)

                    storeSchedule(schedule, items, update)
                    schedule
                }
                ScheduleType.EXAMS -> {
                    val itemsDTOList = scheduleDTO.exam ?: emptyList()
                    val schedule = Schedule.GroupExams(
                            name = group.number,
                            lastUpdatedDate = lastUpdatedDate,
                            group = group,
                            notRemindForUpdates = false
                    )
                    val items = itemsDTOList.toGroupExams(auditories, departments)

                    storeSchedule(schedule, items, update)
                    schedule
                }
            }
        }
    }

    private suspend fun loadEmployeeScheduleInternal(
            employee: Employee,
            types: List<ScheduleType>,
            update: Boolean
    ): List<Schedule> {
        val groups = groupRepository.getAll().first()
        val auditories = buildingRepository.getAllAuditories().first()

        val scheduleDTO = performApiCall { api.getEmployeeSchedule(employee.id) }

        return types.map { type ->
            when (type) {
                ScheduleType.CLASSES -> {
                    val itemsDTOList = scheduleDTO.schedule ?: emptyList()
                    val schedule = Schedule.EmployeeClasses(
                            name = employee.abbreviation,
                            employee = employee
                    )
                    val items = itemsDTOList.toEmployeeLessons(groups, auditories)

                    storeSchedule(schedule, items, update)
                    schedule
                }
                ScheduleType.EXAMS -> {
                    val itemsDTOList = scheduleDTO.exam ?: emptyList()
                    val schedule = Schedule.EmployeeExams(
                            name = employee.abbreviation,
                            employee = employee
                    )
                    val items = itemsDTOList.toEmployeeExams(groups, auditories)

                    storeSchedule(schedule, items, update)
                    schedule
                }
            }
        }
    }

    private suspend fun storeSchedule(
            schedule: Schedule.GroupClasses,
            items: List<Lesson.GroupLesson>,
            update: Boolean
    ) {
        val cachedSchedule = schedule.toDatabaseEntity()
        val cachedItems = items.map { it.toDatabaseEntity(cachedSchedule) }
        performDaoCall {
            if (update) {
                dao.updateGroupClassesSchedule(cachedSchedule, cachedItems)
            } else {
                dao.insertGroupClassesSchedule(cachedSchedule, cachedItems)
            }
        }
    }

    private suspend fun storeSchedule(
            schedule: Schedule.GroupExams,
            items: List<Exam.GroupExam>,
            update: Boolean
    ) {
        val cachedSchedule = schedule.toDatabaseEntity()
        val cachedItems = items.map { it.toDatabaseEntity(cachedSchedule) }
        performDaoCall {
            if (update) {
                dao.updateGroupExamsSchedule(cachedSchedule, cachedItems)
            } else {
                dao.insertGroupExamsSchedule(cachedSchedule, cachedItems)
            }
        }
    }

    private suspend fun storeSchedule(
            schedule: Schedule.EmployeeClasses,
            items: List<Lesson.EmployeeLesson>,
            update: Boolean
    ) {
        val cachedSchedule = schedule.toDatabaseEntity()
        val cachedItems = items.map { it.toDatabaseEntity(cachedSchedule) }
        performDaoCall {
            if (update) {
                dao.updateEmployeeClassesSchedule(cachedSchedule, cachedItems)
            } else {
                dao.insertEmployeeClassesSchedule(cachedSchedule, cachedItems)
            }
        }
    }

    private suspend fun storeSchedule(
            schedule: Schedule.EmployeeExams,
            items: List<Exam.EmployeeExam>,
            update: Boolean
    ) {
        val cachedSchedule = schedule.toDatabaseEntity()
        val cachedItems = items.map { it.toDatabaseEntity(cachedSchedule) }
        performDaoCall {
            if (update) {
                dao.updateEmployeeExamsSchedule(cachedSchedule, cachedItems)
            } else {
                dao.insertEmployeeExamsSchedule(cachedSchedule, cachedItems)
            }
        }
    }
}