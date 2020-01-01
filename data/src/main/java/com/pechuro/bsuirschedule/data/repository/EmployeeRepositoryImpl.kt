package com.pechuro.bsuirschedule.data.repository

import com.pechuro.bsuirschedule.domain.entity.Employee
import com.pechuro.bsuirschedule.domain.repository.IEmployeeRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class EmployeeRepositoryImpl : IEmployeeRepository {

    override fun getAll(): Observable<List<Employee>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getById(id: Long): Single<Employee> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAll(): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isStored(): Single<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}