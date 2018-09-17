package com.pechuro.bsuirschedule.data

import com.pechuro.bsuirschedule.data.database.dao.EmployeeDao
import com.pechuro.bsuirschedule.data.entity.Employee
import com.pechuro.bsuirschedule.data.network.EmployeeApi
import io.reactivex.Single
import javax.inject.Inject

class EmployeeRepository @Inject constructor(private val api: EmployeeApi,
                                             private val dao: EmployeeDao) {
    val isCacheNotEmpty get() = dao.isNotEmpty()

    fun getEmployees(): Single<List<Employee>> =
            getFromDb().onErrorResumeNext { getFromApi() }

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
