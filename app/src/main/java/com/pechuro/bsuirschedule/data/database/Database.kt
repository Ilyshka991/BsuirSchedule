package com.pechuro.bsuirschedule.data.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.pechuro.bsuirschedule.data.database.dao.EmployeeDao
import com.pechuro.bsuirschedule.data.database.dao.GroupDao
import com.pechuro.bsuirschedule.data.database.dao.ScheduleDao
import com.pechuro.bsuirschedule.data.database.dao.ScheduleItemDao
import com.pechuro.bsuirschedule.data.entity.Employee
import com.pechuro.bsuirschedule.data.entity.Group
import com.pechuro.bsuirschedule.data.entity.Schedule
import com.pechuro.bsuirschedule.data.entity.ScheduleItem


@Database(entities = [Group::class, Employee::class, Schedule::class, ScheduleItem::class],
        version = 6)
abstract class AppDatabase : RoomDatabase() {
    abstract fun groupDao(): GroupDao

    abstract fun employeeDao(): EmployeeDao

    abstract fun scheduleDao(): ScheduleDao

    abstract fun scheduleItemDao(): ScheduleItemDao
}