package com.pechuro.bsuirschedule.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pechuro.bsuirschedule.local.dao.*
import com.pechuro.bsuirschedule.local.entity.*

@Database(
        entities = [
            GroupDB::class,
            EmployeeDB::class,
            ScheduleDB::class,
            ScheduleItemDB::class,
            AuditoryDB::class,
            AuditoryTypeDB::class,
            BuildingDB::class,
            DepartmentDB::class,
            FacultyDB::class,
            ScheduleItemAuditoryJoinDB::class,
            ScheduleItemEmployeeJoinDB::class,
            SpecialityDB::class
        ],
        exportSchema = false,
        version = BuildConfig.DATABASE_VERSION)
abstract class AppDatabase : RoomDatabase() {

    abstract fun groupDao(): GroupDao

    abstract fun employeeDao(): EmployeeDao

    abstract fun scheduleDao(): ScheduleDao

    abstract fun buildingDao(): BuildingDao

    abstract fun specialityDao(): SpecialityDao
}