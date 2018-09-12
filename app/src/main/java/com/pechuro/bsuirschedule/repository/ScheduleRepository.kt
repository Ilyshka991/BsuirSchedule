package com.pechuro.bsuirschedule.repository

import com.pechuro.bsuirschedule.repository.api.LastUpdateResponse
import com.pechuro.bsuirschedule.repository.api.Response
import com.pechuro.bsuirschedule.repository.api.ResponseError
import com.pechuro.bsuirschedule.repository.api.ScheduleApi
import com.pechuro.bsuirschedule.repository.db.dao.ScheduleDao
import com.pechuro.bsuirschedule.repository.entity.ScheduleItem
import com.pechuro.bsuirschedule.repository.entity.complex.Classes
import io.reactivex.Single

class ScheduleRepository(private val api: ScheduleApi,
                         private val dao: ScheduleDao) {

    fun loadClasses(group: String): Single<Classes> =
            loadClassesFromCache(group).onErrorResumeNext { loadClassesFromApi(group) }

    private fun loadClassesFromApi(studentGroup: String): Single<Classes> {
        val response = api.getStudentSchedule(studentGroup)
                .onErrorReturn { ResponseError(it) }.blockingGet()
        val lastUpdate = api.getLastUpdateDate(studentGroup)
                .onErrorReturn { LastUpdateResponse("") }
                .blockingGet().lastUpdateDate

        if (response is ResponseError) {
            return Single.error { response.error }
        }

        val studentClasses = Classes(studentGroup, 0, lastUpdate)
        studentClasses.classes = getScheduleItems(response)
        return Single.just(studentClasses)
                .doOnSuccess { storeInDb(it) }
    }

    private fun loadClassesFromCache(group: String) = dao.get(group)

    private fun getScheduleItems(response: Response): List<ScheduleItem> {
        val schedule = ArrayList<ScheduleItem>()

        //Add weekDay to all scheduleItems
        response.schedule?.forEach {
            for (item in it.classes) {
                schedule.add(item)
                schedule[schedule.size - 1].weekDay = it.weekDay
            }
        }
        return schedule
    }

    private fun storeInDb(schedule: Classes) =
            dao.insertSchedule(schedule)
}
