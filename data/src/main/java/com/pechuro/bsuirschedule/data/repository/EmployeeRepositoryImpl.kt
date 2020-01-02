package com.pechuro.bsuirschedule.data.repository

import com.pechuro.bsuirschedule.domain.entity.Employee
import com.pechuro.bsuirschedule.domain.repository.IEmployeeRepository
import kotlinx.coroutines.flow.Flow

class EmployeeRepositoryImpl : IEmployeeRepository {

    override suspend fun getAll(): Flow<List<Employee>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getById(id: Long): Employee {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun update() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteAll() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun isStored(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}