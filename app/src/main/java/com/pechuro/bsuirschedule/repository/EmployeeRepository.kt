package com.pechuro.bsuirschedule.repository

import com.pechuro.bsuirschedule.repository.api.EmployeeApi
import com.pechuro.bsuirschedule.repository.db.dao.EmployeeDao
import com.pechuro.bsuirschedule.repository.entity.Employee
import io.reactivex.Single
import org.jetbrains.anko.doAsync

class EmployeeRepository(private val api: EmployeeApi, private val dao: EmployeeDao) {

    fun getEmployees(): Single<List<Employee>> =
            getFromDb().onErrorResumeNext(getFromApi())


    private fun getFromDb(): Single<List<Employee>> =
            dao.getEmployees().filter { it.isNotEmpty() }.toSingle()

    private fun getFromApi(): Single<List<Employee>> =
            api.getEmployees().doOnSuccess { storeInDb(it) }

    private fun storeInDb(groups: List<Employee>) =
            doAsync { dao.insert(groups) }
}
