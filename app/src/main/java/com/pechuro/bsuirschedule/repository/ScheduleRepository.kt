package com.pechuro.bsuirschedule.repository

import com.pechuro.bsuirschedule.repository.api.Response
import com.pechuro.bsuirschedule.repository.api.ScheduleApi
import com.pechuro.bsuirschedule.repository.db.dao.ScheduleDao
import com.pechuro.bsuirschedule.repository.entity.ScheduleItem
import com.pechuro.bsuirschedule.repository.entity.complex.Classes
import io.reactivex.Observable
import org.jetbrains.anko.doAsync

class ScheduleRepository(private val api: ScheduleApi,
                         private val dao: ScheduleDao) {

    fun loadClasses(number: String): Observable<Classes> {
        return Observable.fromCallable {
            val response = api.getSchedule(number).blockingGet()
            val lastUpdate = api.getLastUpdateDate(number).blockingGet().lastUpdateDate
            val studentClasses = Classes(number, 0, lastUpdate)

            studentClasses.classes = getScheduleItems(response)
            return@fromCallable studentClasses
        }.doOnNext { storeInDb(it) }
    }

    private fun getScheduleItems(response: Response): List<ScheduleItem> {
        val schedule = ArrayList<ScheduleItem>()

        //Add weekDay to all scheduleItems
        response.schedule.forEach {
            for (item in it.classes) {
                schedule.add(item)
                schedule[schedule.size - 1].weekDay = it.weekDay
            }
        }
        return schedule
    }

    private fun storeInDb(groups: Classes) =
            doAsync { dao.insertSchedule(groups) }
}
