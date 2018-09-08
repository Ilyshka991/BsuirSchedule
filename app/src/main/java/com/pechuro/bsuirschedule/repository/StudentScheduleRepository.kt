package com.pechuro.bsuirschedule.repository

import com.pechuro.bsuirschedule.repository.api.StudentScheduleApi
import com.pechuro.bsuirschedule.repository.db.dao.StudentScheduleDao
import com.pechuro.bsuirschedule.repository.entity.Employee
import com.pechuro.bsuirschedule.repository.entity.complex.StudentClasses
import com.pechuro.bsuirschedule.repository.entity.complex.StudentExams
import io.reactivex.Observable
import io.reactivex.Single
import org.jetbrains.anko.doAsync

class StudentScheduleRepository(private val api: StudentScheduleApi,
                                private val dao: StudentScheduleDao) {

    fun loadClasses(number: String): Observable<StudentClasses> {
        return Observable.fromCallable {
            val studentClasses = StudentClasses(number, 0, "null")
            studentClasses.classes = api.getSchedule(number).blockingGet()
            return@fromCallable studentClasses
        }.doOnNext { storeInDb(it) }
    }


    private fun storeInDb(groups: StudentClasses) =
            doAsync { dao.insertSchedule(groups) }
}
