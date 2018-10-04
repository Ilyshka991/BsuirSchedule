package com.pechuro.bsuirschedule.data

import com.pechuro.bsuirschedule.constant.ScheduleType.EMPLOYEE_CLASSES
import com.pechuro.bsuirschedule.constant.ScheduleType.EMPLOYEE_EXAMS
import com.pechuro.bsuirschedule.constant.ScheduleType.STUDENT_CLASSES
import com.pechuro.bsuirschedule.constant.ScheduleType.STUDENT_EXAMS
import com.pechuro.bsuirschedule.data.database.dao.ScheduleDao
import com.pechuro.bsuirschedule.data.entity.ScheduleItem
import com.pechuro.bsuirschedule.data.entity.complex.Classes
import com.pechuro.bsuirschedule.data.network.*
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ScheduleRepository @Inject constructor(private val api: ScheduleApi,
                                             private val dao: ScheduleDao) {

    fun loadClasses(name: String, type: Int) =
            getFromApi(name, type)

    fun getClasses(name: String, type: Int, day: String, week: Int): Single<List<ScheduleItem>> =
            dao.get(name, type, day, week.toString())

    fun getSchedules() = dao.getSchedules()

    fun getNotAddedGroups(type: Int) = dao.getNotAddedGroups(type)

    fun getNotAddedEmployees(type: Int) = dao.getNotAddedEmployees(type)

    fun delete(name: String, type: Int) = dao.delete(name, type)

    fun delete(type: Int) = dao.delete(type)

    private fun getFromApi(name: String, type: Int): Single<Classes> {
        lateinit var response: Response

        when (type) {
            STUDENT_CLASSES, STUDENT_EXAMS ->
                response = api.getStudentSchedule(name)
                        .subscribeOn(Schedulers.io())
                        .onErrorReturn { ResponseError(it) }
                        .blockingGet()
            EMPLOYEE_CLASSES, EMPLOYEE_EXAMS ->
                response = api.getEmployeeSchedule(name)
                        .subscribeOn(Schedulers.io())
                        .onErrorReturn { ResponseError(it) }
                        .blockingGet()
        }

        val lastUpdate = api.getLastUpdateDate(name)
                .subscribeOn(Schedulers.io())
                .onErrorReturn { LastUpdateResponse(null) }
                .blockingGet().lastUpdateDate

        if (response is ResponseError) {
            return Single.error(response.error)
        }

        val classes = Classes(name, type, lastUpdate)

        when (type) {
            STUDENT_CLASSES, EMPLOYEE_CLASSES ->
                classes.schedule = getScheduleItems(response.schedule)
            STUDENT_EXAMS, EMPLOYEE_EXAMS ->
                classes.schedule = getScheduleItems(response.exam)
        }

        return Single.just(classes).doOnSuccess { storeInCache(it) }
    }

    private fun getScheduleItems(response: List<ScheduleResponse>?): List<ScheduleItem> {
        val schedule = ArrayList<ScheduleItem>()

        //Add weekDay to all scheduleItems
        response?.forEach {
            for (item in it.classes) {
                schedule.add(item)
                schedule[schedule.size - 1].weekDay = it.weekDay.toLowerCase()
            }
        }
        return schedule
    }

    private fun storeInCache(schedule: Classes) =
            dao.insertSchedule(schedule)
}
