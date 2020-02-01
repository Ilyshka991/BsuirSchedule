package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.common.Logger
import com.pechuro.bsuirschedule.domain.entity.Employee
import com.pechuro.bsuirschedule.domain.entity.Group
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import com.pechuro.bsuirschedule.domain.repository.IEmployeeRepository
import com.pechuro.bsuirschedule.domain.repository.IGroupRepository
import com.pechuro.bsuirschedule.domain.repository.IScheduleRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class LoadAllSchedules @Inject constructor(
        private val scheduleRepository: IScheduleRepository,
        private val groupRepository: IGroupRepository,
        private val employeeRepository: IEmployeeRepository
) : BaseInteractor<Unit, BaseInteractor.NoParams>() {

    override suspend fun run(params: NoParams) {
        val groupsResult = tryLoadAllGroupSchedule()
        groupsResult.mapValues { it.value.map { it.number } }.forEach {
            Logger.d("ERROR GROUPS: ${it.key} - ${it.value.joinToString()}")
        }
        val employeeResult = tryLoadAllEmployeesSchedule()
        employeeResult.mapValues { it.value.map { it.abbreviation } }.forEach {
            Logger.d("ERROR EMPLOYEES: ${it.key} - ${it.value.joinToString()}")
        }
    }

    private suspend fun tryLoadAllGroupSchedule(): Map<Class<out Throwable>, List<Group>> {
        val groups = groupRepository.getAll().first()
        val errorGroups = mutableListOf<Pair<Throwable, Group>>()
        withContext(coroutineContext) {
            groups.forEach { group ->
                launch {
                    runCatching {
                        scheduleRepository.loadGroupSchedule(
                                group,
                                listOf(ScheduleType.EXAMS, ScheduleType.CLASSES)
                        )
                    }.onFailure {
                        errorGroups += it to group
                    }
                }
            }
        }
        return errorGroups
                .groupBy { it.first::class.java }
                .mapValues { it.value.map { it.second } }
    }

    private suspend fun tryLoadAllEmployeesSchedule(): Map<Class<out Throwable>, List<Employee>> {
        val employees = employeeRepository.getAll().first()
        val errorEmployees = mutableListOf<Pair<Throwable, Employee>>()
        withContext(coroutineContext) {
            employees.forEach { employee ->
                launch {
                    runCatching {
                        scheduleRepository.loadEmployeeSchedule(
                                employee,
                                listOf(ScheduleType.EXAMS, ScheduleType.CLASSES)
                        )
                    }.onFailure {
                        errorEmployees += it to employee
                    }
                }
            }
        }
        return errorEmployees
                .groupBy { it.first::class.java }
                .mapValues { it.value.map { it.second } }
    }
}