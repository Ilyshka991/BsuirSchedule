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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

class EmployeeRepositoryImpl(
        private val dao: EmployeeDao,
        private val api: StaffApi
) : BaseRepository(), IEmployeeRepository {

    override suspend fun getAll(forceUpdate: Boolean): Flow<List<Employee>> {
        withContext(coroutineContext) {
            launch {
                if (forceUpdate || !isCached()) {
                    updateCache()
                }
            }
        }
        return getEmployeesFromDao()
    }

    override suspend fun getById(id: Long): Employee =
            performDaoCall { dao.getById(id) }.toDomainEntity()

    override suspend fun updateCache() {
        val loadedEmployees = loadEmployeesFromApi()
        deleteAll()
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
            .map { list ->
                list.map { employeeDb ->
                    employeeDb.toDomainEntity()
                }
            }

    private suspend fun storeEmployees(employees: List<Employee>) {
        employees.map {
            it.toDatabaseEntity()
        }.run {
            performDaoCall { dao.insert(this) }
        }
    }
}