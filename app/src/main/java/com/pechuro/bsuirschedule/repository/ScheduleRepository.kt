package com.pechuro.bsuirschedule.repository

import com.pechuro.bsuirschedule.constant.ScheduleType.EMPLOYEE_CLASSES
import com.pechuro.bsuirschedule.constant.ScheduleType.EMPLOYEE_EXAMS
import com.pechuro.bsuirschedule.constant.ScheduleType.STUDENT_CLASSES
import com.pechuro.bsuirschedule.constant.ScheduleType.STUDENT_EXAMS
import com.pechuro.bsuirschedule.repository.api.*
import com.pechuro.bsuirschedule.repository.db.dao.ScheduleDao
import com.pechuro.bsuirschedule.repository.entity.ScheduleItem
import com.pechuro.bsuirschedule.repository.entity.complex.Classes
import io.reactivex.Single

class ScheduleRepository(private val api: ScheduleApi,
                         private val dao: ScheduleDao) {

    fun getClasses(name: String, type: Int): Single<Classes> =
            getFromCache(name, type).onErrorResumeNext { getFromApi(name, type) }

    fun getNotAddedGroups(type: Int) = dao.getNotAddedGroups(type)

    fun getNotAddedEmployees(type: Int) = dao.getNotAddedEmployees(type)

    fun delete(name: String, type: Int) = dao.delete(name, type)

    fun delete(type: Int) = dao.delete(type)

    private fun getFromApi(name: String, type: Int): Single<Classes> {
        lateinit var response: Response

        when (type) {
            STUDENT_CLASSES, STUDENT_EXAMS ->
                response = api.getStudentSchedule(name)
                        .onErrorReturn { ResponseError(it) }.blockingGet()
            EMPLOYEE_CLASSES, EMPLOYEE_EXAMS ->
                response = api.getEmployeeSchedule(name)
                        .onErrorReturn { ResponseError(it) }.blockingGet()
        }

        val lastUpdate = api.getLastUpdateDate(name)
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

        return Single.just(classes)
                .doOnSuccess { storeInCache(it) }
    }

    private fun getFromCache(name: String, type: Int) = dao.get(name, type)

    private fun getScheduleItems(response: List<ScheduleResponse>?): List<ScheduleItem> {
        val schedule = ArrayList<ScheduleItem>()

        //Add weekDay to all scheduleItems
        response?.forEach {
            for (item in it.classes) {
                schedule.add(item)
                schedule[schedule.size - 1].weekDay = it.weekDay
            }
        }
        return schedule
    }

    private fun storeInCache(schedule: Classes) =
            dao.insertSchedule(schedule)
}
