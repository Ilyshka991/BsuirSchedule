package com.pechuro.bsuirschedule.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pechuro.bsuirschedule.local.database.dao.EmployeeDao
import com.pechuro.bsuirschedule.local.database.dao.GroupDao
import com.pechuro.bsuirschedule.local.database.dao.ScheduleDao
import com.pechuro.bsuirschedule.local.database.entity.Employee
import com.pechuro.bsuirschedule.local.database.entity.Group
import com.pechuro.bsuirschedule.local.database.entity.Schedule
import com.pechuro.bsuirschedule.local.database.entity.ScheduleItem

@Database(entities = [Group::class, Employee::class, Schedule::class, ScheduleItem::class],
        version = 8)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun groupDao(): GroupDao

    abstract fun employeeDao(): EmployeeDao

    abstract fun scheduleDao(): ScheduleDao
}