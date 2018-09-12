package com.pechuro.bsuirschedule.repository

import com.pechuro.bsuirschedule.repository.api.EmployeeApi
import com.pechuro.bsuirschedule.repository.db.dao.EmployeeDao
import com.pechuro.bsuirschedule.repository.entity.Employee
import io.reactivex.Single

class EmployeeRepository(private val api: EmployeeApi, private val dao: EmployeeDao) {
    val isCacheNotEmpty get() = dao.isNotEmpty()

    fun getEmployees(): Single<List<Employee>> =
            getFromDb().onErrorResumeNext(getFromApi())

    fun delete() = dao.delete()

    fun getByFio(fio: String) = dao.get(fio)

    fun getNames() = dao.getNames()

    fun getIdByFio(fio: String) = dao.getId(fio)

    private fun getFromDb(): Single<List<Employee>> =
            dao.get().filter { it.isNotEmpty() }.toSingle()

    private fun getFromApi(): Single<List<Employee>> =
            api.get().doOnSuccess { storeInDb(it) }

    private fun storeInDb(groups: List<Employee>) = dao.insert(groups)
}
