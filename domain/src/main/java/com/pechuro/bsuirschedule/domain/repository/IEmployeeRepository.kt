package com.pechuro.bsuirschedule.domain.repository

import com.pechuro.bsuirschedule.domain.entity.Employee
import io.reactivex.Completable
import io.reactivex.Single

interface IEmployeeRepository {

    fun getAll(): Single<List<Employee>>

    fun getById(id: Long): Single<Employee>

    fun deleteAll(): Completable

    fun isStored(): Single<Boolean>
}
