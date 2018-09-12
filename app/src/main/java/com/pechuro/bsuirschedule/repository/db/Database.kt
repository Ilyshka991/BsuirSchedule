package com.pechuro.bsuirschedule.repository.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.pechuro.bsuirschedule.repository.db.dao.EmployeeDao
import com.pechuro.bsuirschedule.repository.db.dao.GroupDao
import com.pechuro.bsuirschedule.repository.db.dao.ScheduleDao
import com.pechuro.bsuirschedule.repository.db.dao.ScheduleItemDao
import com.pechuro.bsuirschedule.repository.entity.Employee
import com.pechuro.bsuirschedule.repository.entity.Group
import com.pechuro.bsuirschedule.repository.entity.Schedule
import com.pechuro.bsuirschedule.repository.entity.ScheduleItem


@Database(entities = [Group::class, Employee::class, Schedule::class, ScheduleItem::class],
        version = 6)
abstract class AppDatabase : RoomDatabase() {
    abstract fun groupDao(): GroupDao

    abstract fun employeeDao(): EmployeeDao

    abstract fun scheduleDao(): ScheduleDao

    abstract fun scheduleItemDao(): ScheduleItemDao
}