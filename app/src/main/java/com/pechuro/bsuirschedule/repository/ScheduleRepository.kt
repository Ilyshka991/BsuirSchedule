package com.pechuro.bsuirschedule.repository

import com.pechuro.bsuirschedule.repository.api.ScheduleApi
import com.pechuro.bsuirschedule.repository.db.dao.ScheduleDao
import com.pechuro.bsuirschedule.repository.entity.Classes
import io.reactivex.Observable
import org.jetbrains.anko.doAsync

class ScheduleRepository(private val api: ScheduleApi,
                         private val dao: ScheduleDao) {

    fun loadClasses(number: String): Observable<Classes> {
        return Observable.fromCallable {
            val studentClasses = Classes(number, 0, "null")
            studentClasses.classes = api.getSchedule(number).blockingGet().schedules[0].classes
            return@fromCallable studentClasses
        }.doOnNext { storeInDb(it) }
    }


    private fun storeInDb(groups: Classes) =
            doAsync { dao.insertSchedule(groups) }
}
