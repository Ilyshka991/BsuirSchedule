package com.pechuro.bsuirschedule.data.repository

import com.pechuro.bsuirschedule.data.common.BaseRepository
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
) : BaseRepository(), IEmployeeRepository {

    override suspend fun getAll(): Flow<List<Employee>> {
        if (!isCached()) {
            updateCache()
        }
        return getEmployeesFromDao()
    }

    override suspend fun getAllNames(): Flow<List<String>> {
        return performDaoCall { dao.getAllNames() }
    }

    override suspend fun getById(id: Long): Employee =
            performDaoCall { dao.getById(id) }.toDomainEntity()

    override suspend fun updateCache() {
        val loadedEmployees = loadEmployeesFromApi()
        storeEmployees(loadedEmployees)
    }

    override suspend fun deleteAll() {
        performDaoCall { dao.deleteAll() }
    }

    override suspend fun isCached(): Boolean =
            performDaoCall { dao.isNotEmpty() }

    private suspend fun loadEmployeesFromApi(): List<Employee> =
            performApiCall { api.getAllEmployees() }
                    .map { dto ->
                        dto.toDomainEntity()
                    }

    private suspend fun getEmployeesFromDao() = performDaoCall { dao.getAll() }
            .map { cachedList ->
                cachedList.map { employeeCached ->
                    employeeCached.toDomainEntity()
                }
            }

    private suspend fun storeEmployees(employees: List<Employee>) {
        employees.map {
            it.toDatabaseEntity()
        }.run {
            performDaoCall { dao.insertOrUpdate(this) }
        }
    }
}