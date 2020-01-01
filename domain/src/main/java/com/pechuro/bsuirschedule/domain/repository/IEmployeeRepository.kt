package com.pechuro.bsuirschedule.domain.repository

import com.pechuro.bsuirschedule.domain.entity.Employee
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface IEmployeeRepository {

    fun getAll(): Observable<List<Employee>>

    fun getById(id: Long): Single<Employee>

    fun update(): Completable

    fun deleteAll(): Completable

    fun isStored(): Single<Boolean>
}
