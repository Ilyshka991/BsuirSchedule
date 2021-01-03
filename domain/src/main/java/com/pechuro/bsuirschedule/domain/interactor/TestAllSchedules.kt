package com.pechuro.bsuirschedule.domain.interactor

import com.pechuro.bsuirschedule.domain.common.BaseInteractor
import com.pechuro.bsuirschedule.domain.common.Logger
import com.pechuro.bsuirschedule.domain.entity.Employee
import com.pechuro.bsuirschedule.domain.entity.Group
import com.pechuro.bsuirschedule.domain.entity.Schedule
import com.pechuro.bsuirschedule.domain.entity.ScheduleType
import com.pechuro.bsuirschedule.domain.repository.IEmployeeRepository
import com.pechuro.bsuirschedule.domain.repository.IGroupRepository
import com.pechuro.bsuirschedule.domain.repository.IScheduleRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class TestAllSchedules @Inject constructor(
        private val scheduleRepository: IScheduleRepository,
        private val groupRepository: IGroupRepository,
        private val employeeRepository: IEmployeeRepository
) : BaseInteractor<Unit, BaseInteractor.NoParams>() {

    override suspend fun run(params: NoParams) {
        //  tryLoadAllGroupSchedules()
        // tryLoadAllEmployeesSchedules()
        //checkEmptySchedules()
        scheduleRepository.getAllSchedules().first().filter {
            it is Schedule.GroupClasses && it.group.speciality.educationForm.name == "дистанционная"
        }.also {
            Logger.d(it.joinToString { it.name })
        }
    }

    private suspend fun tryLoadAllGroupSchedules() {
        val groups = groupRepository.getAll().first()
        val errorGroups = mutableListOf<Pair<Throwable, Group>>()
        groups.forEach { group ->
            runCatching {
                scheduleRepository.loadGroupSchedule(
                        group,
                        listOf(ScheduleType.EXAMS, ScheduleType.CLASSES)
                )
            }.onFailure {
                errorGroups += it to group
            }
        }
        errorGroups
                .groupBy { it.first::class.java }
                .mapValues { it.value.map { it.second } }
                .mapValues { it.value.map { it.number } }
                .forEach {
                    Logger.d("ERROR GROUPS: ${it.key} - ${it.value.joinToString()}")
                }
    }

    private suspend fun tryLoadAllEmployeesSchedules() {
        val employees = employeeRepository.getAll().first()
        val errorEmployees = mutableListOf<Pair<Throwable, Employee>>()
        employees.forEach { employee ->
            runCatching {
                scheduleRepository.loadEmployeeSchedule(
                        employee,
                        listOf(ScheduleType.EXAMS, ScheduleType.CLASSES)
                )
            }.onFailure {
                errorEmployees += it to employee
            }
        }
        errorEmployees
                .groupBy { it.first::class.java }
                .mapValues { it.value.map { it.second } }
                .mapValues { it.value.map { it.abbreviation } }
                .forEach {
                    Logger.d("ERROR EMPLOYEES: ${it.key} - ${it.value.joinToString()}")
                }
    }

    private suspend fun checkEmptySchedules() {
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