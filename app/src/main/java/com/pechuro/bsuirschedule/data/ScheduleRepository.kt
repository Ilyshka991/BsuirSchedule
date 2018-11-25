package com.pechuro.bsuirschedule.data

import com.pechuro.bsuirschedule.constants.ScheduleTypes.EMPLOYEE_CLASSES
import com.pechuro.bsuirschedule.constants.ScheduleTypes.EMPLOYEE_EXAMS
import com.pechuro.bsuirschedule.constants.ScheduleTypes.STUDENT_CLASSES
import com.pechuro.bsuirschedule.constants.ScheduleTypes.STUDENT_EXAMS
import com.pechuro.bsuirschedule.data.database.dao.EmployeeDao
import com.pechuro.bsuirschedule.data.database.dao.ScheduleDao
import com.pechuro.bsuirschedule.data.entity.ScheduleItem
import com.pechuro.bsuirschedule.data.entity.complex.Classes
import com.pechuro.bsuirschedule.data.network.LastUpdateResponse
import com.pechuro.bsuirschedule.data.network.ResponseModel
import com.pechuro.bsuirschedule.data.network.ScheduleApi
import com.pechuro.bsuirschedule.data.network.ScheduleResponse
import com.pechuro.bsuirschedule.ui.data.ScheduleInformation
import com.pechuro.bsuirschedule.ui.utils.getInfo
import io.reactivex.Single
import javax.inject.Inject

class ScheduleRepository @Inject constructor(private val api: ScheduleApi,
                                             private val scheduleDao: ScheduleDao,
                                             private val employeeDao: EmployeeDao) {

    fun loadClasses(name: String, types: List<Int>): Single<MutableList<Classes>> =
            Single.fromCallable {
                val response = getFromApi(name, types[0])

                val lastUpdate = getLastUpdate(name)
                        .onErrorReturn { LastUpdateResponse("") }
                        .blockingGet().lastUpdateDate

                deleteLastSchedules(name, types)

                val classesList = mutableListOf<Classes>()
                types.forEach { type ->
                    classesList.add(transformResponse(name, type, lastUpdate, response))
                }
                classesList
            }.doOnSuccess { storeInCache(it) }

    fun update(info: ScheduleInformation): Single<Classes> =
            Single.fromCallable {
                val response = getFromApi(info.name, info.type)

                val lastUpdate = getLastUpdate(info.name)
                        .onErrorReturn { LastUpdateResponse("") }
                        .blockingGet().lastUpdateDate

                transformResponse(info.name, info.type, lastUpdate, response, info.id)
            }.doOnSuccess { updateCache(it) }

    fun getClasses(name: String, type: Int) =
            scheduleDao.get(name, type)

    fun getStudentClasses(name: String, type: Int, day: String, week: Int, subgroup: Int) =
            scheduleDao.get(name, type, day, week.toString(), if (subgroup == 0) arrayOf(0, 1, 2) else arrayOf(0, subgroup))

    fun getStudentClasses(name: String, type: Int, day: String, subgroup: Int) =
            scheduleDao.get(name, type, day, if (subgroup == 0) arrayOf(0, 1, 2) else arrayOf(0, subgroup))

    fun getEmployeeClasses(name: String, type: Int, day: String) =
            scheduleDao.get(name, type, day)

    fun getEmployeeClasses(name: String, type: Int, day: String, week: Int) =
            scheduleDao.get(name, type, day, week.toString())

    fun getSchedules() = scheduleDao.getSchedules()

    fun delete(name: String, type: Int): Single<Unit> = Single.fromCallable {
        scheduleDao.delete(name, type)
    }

    fun deleteItem(id: Int): Single<Unit> = Single.fromCallable {
        scheduleDao.deleteItem(id)
    }

    fun getNotUpdatedSchedules(): Single<List<ScheduleInformation>> = Single.fromCallable {
        val schedules = scheduleDao.getStudentSchedules().blockingGet()
        val schedulesForUpdate = mutableListOf<ScheduleInformation>()
        schedules.forEach {
            val currentUpdate = getLastUpdate(it.name).blockingGet().lastUpdateDate
            if (currentUpdate != it.lastUpdate) {
                schedulesForUpdate += it.getInfo()
            }
        }
        schedulesForUpdate
    }

    private fun getLastUpdate(name: String): Single<LastUpdateResponse> = api.getLastUpdateDate(name)

    private fun deleteLastSchedules(name: String, types: List<Int>) = types.forEach { scheduleDao.delete(name, it) }

    private fun transformResponse(name: String, type: Int, lastUpdate: String, response: ResponseModel, id: Int = -1): Classes {
        fun getScheduleItems(response: List<ScheduleResponse>?): List<ScheduleItem> {
            val scheduleItems = ArrayList<ScheduleItem>()
            response?.forEach {
                for (item in it.classes) {
                    scheduleItems.add(item)
                    scheduleItems[scheduleItems.size - 1].weekDay = it.weekDay.toLowerCase()

                    val auditoriesSize = item.auditories?.size
                    val employeesSize = item.employees?.size
                    if (auditoriesSize != null && employeesSize != null && auditoriesSize < employeesSize) {
                        val difference = employeesSize - auditoriesSize
                        for (i in 1..difference) {
                            item.auditories.add("")
                        }
                    }
                }
            }
            return scheduleItems
        }

        val classes = Classes(name, type, lastUpdate)

        if (id != -1) {
            classes.id = id
        }

        classes.schedule = when (classes.type) {
            STUDENT_CLASSES, EMPLOYEE_CLASSES ->
                getScheduleItems(response.schedule)
            STUDENT_EXAMS, EMPLOYEE_EXAMS ->
                getScheduleItems(response.exam)
            else -> throw IllegalStateException()
        }

        return classes
    }

    private fun getFromApi(name: String, type: Int) = when (type) {
        STUDENT_CLASSES, STUDENT_EXAMS ->
            api.getStudentSchedule(name)
        EMPLOYEE_CLASSES, EMPLOYEE_EXAMS -> {
            val id = employeeDao.getId(name).blockingGet()
            api.getEmployeeSchedule(id)
        }
        else -> throw IllegalStateException()
    }.blockingGet()

    private fun storeInCache(schedule: MutableList<Classes>) =
            schedule.forEach {
                scheduleDao.insertSchedule(it)
            }

    private fun updateCache(classes: Classes) =
            scheduleDao.update(classes)
}
