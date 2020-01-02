package com.pechuro.bsuirschedule.data.repository

import com.pechuro.bsuirschedule.data.mappers.toDatabaseEntity
import com.pechuro.bsuirschedule.data.mappers.toDomainEntity
import com.pechuro.bsuirschedule.domain.entity.Employee
import com.pechuro.bsuirschedule.domain.repository.IEmployeeRepository
import com.pechuro.bsuirschedule.local.dao.EmployeeDao
import com.pechuro.bsuirschedule.remote.api.StaffApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EmployeeRepositoryImpl(
        private val dao: EmployeeDao,
        private val api: StaffApi
) : IEmployeeRepository {

    override suspend fun getAll(): Flow<List<Employee>> {
        if (!isCached()) {
            val loadedEmployees = loadEmployeesFromApi()
            storeEmployees(loadedEmployees)
        }
        return getEmployeesFromDao()
    }

    override suspend fun getById(id: Long): Employee = dao.getById(id).toDomainEntity()

    override suspend fun updateCache() {
        val loadedEmployees = loadEmployeesFromApi()
        deleteAll()
        storeEmployees(loadedEmployees)
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }

    override suspend fun isCached(): Boolean = dao.isNotEmpty()

    private suspend fun loadEmployeesFromApi(): List<Employee> = api.getAllEmployees()
            .map { dto ->
                dto.toDomainEntity()
            }

    private fun getEmployeesFromDao() = dao.getAll().map { list ->
        list.map { employeeDb ->
            employeeDb.toDomainEntity()
        }
    }

    private suspend fun storeEmployees(employees: List<Employee>) {
        employees.map {
            it.toDatabaseEntity()
        }.run {
            dao.insert(this)
        }
    }
}