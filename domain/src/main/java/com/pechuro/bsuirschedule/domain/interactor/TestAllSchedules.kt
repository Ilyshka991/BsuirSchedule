package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.common.Logger
import com.pechuro.bsuirschedule.domain.entity.Employee
import com.pechuro.bsuirschedule.domain.entity.Group
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import com.pechuro.bsuirschedule.domain.entity.correlateScheduleTypes
import com.pechuro.bsuirschedule.domain.repository.IBuildingRepository
import com.pechuro.bsuirschedule.domain.repository.IEmployeeRepository
import com.pechuro.bsuirschedule.domain.repository.IGroupRepository
import com.pechuro.bsuirschedule.domain.repository.IScheduleRepository
import com.pechuro.bsuirschedule.domain.repository.ISpecialityRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class TestAllSchedules @Inject constructor(
        private val scheduleRepository: IScheduleRepository,
        private val groupRepository: IGroupRepository,
        private val employeeRepository: IEmployeeRepository,
        private val specialityRepository: ISpecialityRepository,
        private val buildingRepository: IBuildingRepository
) : BaseInteractor<Unit, BaseInteractor.NoParams>() {

    override suspend fun run(params: NoParams) {
        updateInfo()
        loadAllGroupSchedules()
        loadAllEmployeesSchedules()
        checkScheduleCount()
        checkEmptySchedules()
        updateInfo()
        checkScheduleCount()
        // checkEmptySchedules()
        Logger.d("FINISH")
    }

    private suspend fun loadAllGroupSchedules() {
        Logger.d("CHECK GROUPS SCHEDULES")
        val groups = groupRepository.getAll().first()
        val errorGroups = mutableListOf<Pair<Throwable, Group>>()
        val allTypes = listOf(ScheduleType.EXAMS, ScheduleType.CLASSES)
        var totalCount = groups.size * allTypes.size
        var totalLoaded = 0
        groups.forEach { group ->
            runCatching {
                val resultTypes = group.correlateScheduleTypes(allTypes)
                totalCount -= allTypes.size - resultTypes.size
                val loaded = scheduleRepository.loadGroupSchedule(
                        group,
                        resultTypes
                )
                totalLoaded += loaded.size
            }.onFailure {
                errorGroups += it to group
            }
        }
        Logger.d("GROUPS LOAD FINISHED: $totalLoaded SCHEDULES OF ${groups.size} GROUPS. NOT LOADED: ${totalCount - totalLoaded}")
        errorGroups
                .groupBy { it.first::class.java }
                .mapValues { it.value.map { it.second } }
                .mapValues { it.value.map { it.number } }
                .forEach {
                    Logger.d("ERROR GROUPS: ${it.key} - ${it.value.joinToString()}")
                }
    }

    private suspend fun loadAllEmployeesSchedules() {
        Logger.d("CHECK EMPLOYEES SCHEDULES")
        val employees = employeeRepository.getAll().first()
        val errorEmployees = mutableListOf<Pair<Throwable, Employee>>()
        val allTypes = listOf(ScheduleType.EXAMS, ScheduleType.CLASSES)
        var totalLoaded = 0
        employees.forEach { employee ->
            runCatching {
                val loaded = scheduleRepository.loadEmployeeSchedule(
                        employee,
                        allTypes
                )
                totalLoaded += loaded.size
            }.onFailure {
                errorEmployees += it to employee
            }
        }
        Logger.d("EMPLOYEES LOAD FINISHED: $totalLoaded SCHEDULES OF ${employees.size} EMPLOYEES. NOT LOADED: ${employees.size * allTypes.size - totalLoaded}")
        errorEmployees
                .groupBy { it.first::class.java }
                .mapValues { it.value.map { it.second } }
                .mapValues { it.value.map { it.abbreviation } }
                .forEach {
                    Logger.d("ERROR EMPLOYEES: ${it.key} - ${it.value.joinToString()}")
                }
    }

    private suspend fun updateInfo() = withContext(coroutineContext) {
        Logger.d("UPDATE INFO")
        runCatching {
            specialityRepository.updateCache()
            listOf(
                    async { employeeRepository.updateCache() },
                    async { groupRepository.updateCache() },
                    async { buildingRepository.updateCache() }
            ).awaitAll()
        }.fold(
                onSuccess = {
                    Logger.d("UPDATE INFO SUCCESS")
                },
                onFailure = {
                    Logger.d("UPDATE INFO ERROR", it)
                }
        )
    }

    private suspend fun checkScheduleCount() {
        val allSchedules = scheduleRepository.getAllSchedules().first()
        val groupSchedulesSize = allSchedules.filter { it is Schedule.GroupExams || it is Schedule.GroupClasses }.size
        val employeeSchedulesSize = allSchedules.filter { it is Schedule.EmployeeExams || it is Schedule.EmployeeClasses }.size
        Logger.d("GROUP SCHEDULES COUNT: $groupSchedulesSize")
        Logger.d("EMPLOYEE SCHEDULES COUNT: $employeeSchedulesSize")
    }

    private suspend fun checkEmptySchedules() {
        Logger.d("CHECK EMPTY SCHEDULES")
        val emptySchedules = scheduleRepository.getAllSchedules().first()
                .filter {
                    scheduleRepository.getScheduleItems(it).first().isEmpty()
                }
        val repeated = emptySchedules
                .groupingBy { it.name }
                .eachCount()
                .filter { it.value > 1 }
                .keys
        emptySchedules
                .groupBy {
                    when {
                        it.name in repeated -> "All"
                        it is Schedule.GroupClasses -> {
                            "${it::class.java.simpleName} ${it.group.speciality.educationForm.name}"
                        }
                        else -> it::class.java.simpleName
                    }
                }
                .mapValues { it.value.map { it.name } }
                .forEach {
                    Logger.d("EMPTY SCHEDULES: ${it.key} - ${it.value.joinToString()}")
                }
    }
}