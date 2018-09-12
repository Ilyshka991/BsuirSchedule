package com.pechuro.bsuirschedule.repository

import com.pechuro.bsuirschedule.repository.api.EmployeeApi
import com.pechuro.bsuirschedule.repository.db.dao.EmployeeDao
import com.pechuro.bsuirschedule.repository.entity.Employee
import io.reactivex.Single
import org.jetbrains.anko.doAsync

class EmployeeRepository(private val api: EmployeeApi, private val dao: EmployeeDao) {

    fun getEmployees(): Single<List<Employee>> =
            getFromDb().onErrorResumeNext(getFromApi())

    fun delete() = dao.delete()

    fun getEmployeeByFio(fio: String) = dao.get(fio)

    fun getNames() = dao.getNames()

    fun getIdByFio(fio: String) = dao.getId(fio)

    private fun getFromDb(): Single<List<Employee>> =
            dao.get().filter { it.isNotEmpty() }.toSingle()

    private fun getFromApi(): Single<List<Employee>> =
            api.getEmployees().doOnSuccess { storeInDb(it) }

    private fun storeInDb(groups: List<Employee>) =
            doAsync { dao.insert(groups) }

    private fun isCacheNotEmpty() = dao.isNotEmpty()
}
