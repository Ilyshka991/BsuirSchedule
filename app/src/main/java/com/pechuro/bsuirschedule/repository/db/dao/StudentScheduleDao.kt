package com.pechuro.bsuirschedule.repository.db.dao

import android.arch.persistence.room.*
import com.pechuro.bsuirschedule.repository.entity.Schedule
import com.pechuro.bsuirschedule.repository.entity.complex.StudentClasses
import com.pechuro.bsuirschedule.repository.entity.complex.StudentExams
import com.pechuro.bsuirschedule.repository.entity.item.StudentClassItem
import com.pechuro.bsuirschedule.repository.entity.item.StudentExamItem
import io.reactivex.Single

@Dao
interface StudentScheduleDao {
    @Query("SELECT * FROM all_schedules")
    fun get(): Single<StudentClasses>

    @Transaction
    fun insertSchedule(schedules: StudentClasses) {
        val id: Int = insert(schedules).toInt()
        schedules.classes.forEach { it.scheduleId = id }
        insert(schedules.classes)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(values: Schedule): Long


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(values: List<StudentClassItem>)
}