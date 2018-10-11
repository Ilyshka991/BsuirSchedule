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

    fun loadClasses(name: String, types: List<Int>) =
            getFromApi(name, types)

    fun getClasses(name: String, type: Int, day: String, week: Int) =
            dao.get(name, type, day, week.toString())

    fun getSchedules() = dao.getSchedules()

    fun delete(name: String, type: Int) = dao.delete(name, type)

    fun delete(type: Int) = dao.delete(type)

    private fun getFromApi(name: String, types: List<Int>): Single<MutableList<Classes>> {

        fun getScheduleItems(response: List<ScheduleResponse>?): List<ScheduleItem> {
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

        fun deleteLastSchedules() = types.forEach { dao.delete(name, it) }

        return Single.fromCallable {
            lateinit var response: Response

            when (types[0]) {
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
                throw Throwable(response.error)
            }

            deleteLastSchedules()

            val classesList = mutableListOf<Classes>()
            types.forEach { type ->
                val classes = Classes(name, type, lastUpdate)
                when (type) {
                    STUDENT_CLASSES, EMPLOYEE_CLASSES ->
                        classes.schedule = getScheduleItems(response.schedule)
                    STUDENT_EXAMS, EMPLOYEE_EXAMS ->
                        classes.schedule = getScheduleItems(response.exam)
                }
                classesList.add(classes)
            }
            classesList
        }.doOnSuccess { storeInCache(it) }
    }

    private fun storeInCache(schedule: MutableList<Classes>) =
            schedule.forEach {
                dao.insertSchedule(it)
            }
}
