package com.pechuro.bsuirschedule.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pechuro.bsuirschedule.local.converters.DateConverter
import com.pechuro.bsuirschedule.local.converters.ListConverter
import com.pechuro.bsuirschedule.local.dao.*
import com.pechuro.bsuirschedule.local.entity.*

@Database(
        entities = [
            GroupCached::class,
            EmployeeCached::class,
            ScheduleCached::class,
            ScheduleItemCached::class,
            AuditoryCached::class,
            AuditoryTypeCached::class,
            BuildingCached::class,
            DepartmentCached::class,
            FacultyCached::class,
            SpecialityCached::class,
            EducationFormCached::class,
            ScheduleItemAuditoryCrossRef::class,
            ScheduleItemEmployeeCrossRef::class,
            ScheduleItemGroupCrossRef::class
        ],
        exportSchema = false,
        version = BuildConfig.DATABASE_VERSION)
@TypeConverters(DateConverter::class, ListConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun groupDao(): GroupDao

    abstract fun employeeDao(): EmployeeDao

    abstract fun scheduleDao(): ScheduleDao

    abstract fun buildingDao(): BuildingDao

    abstract fun specialityDao(): SpecialityDao
}