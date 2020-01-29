package com.pechuro.bsuirschedule.domain.repository

import com.pechuro.bsuirschedule.domain.entity.Employee
import kotlinx.coroutines.flow.Flow

interface IEmployeeRepository {

    suspend fun getAll(): Flow<List<Employee>>

    suspend fun getAllNames(): Flow<List<String>>

    suspend fun getById(id: Long): Employee

    suspend fun getIdByName(name: String): Long

    suspend fun deleteAll()

    suspend fun isCached(): Boolean

    suspend fun updateCache()
}
