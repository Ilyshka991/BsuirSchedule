package com.pechuro.bsuirschedule.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pechuro.bsuirschedule.local.converters.DateConverter
import com.pechuro.bsuirschedule.local.dao.*
import com.pechuro.bsuirschedule.local.entity.building.AuditoryCached
import com.pechuro.bsuirschedule.local.entity.building.AuditoryTypeCached
import com.pechuro.bsuirschedule.local.entity.building.BuildingCached
import com.pechuro.bsuirschedule.local.entity.education.DepartmentCached
import com.pechuro.bsuirschedule.local.entity.education.EducationFormCached
import com.pechuro.bsuirschedule.local.entity.education.FacultyCached
import com.pechuro.bsuirschedule.local.entity.education.SpecialityCached
import com.pechuro.bsuirschedule.local.entity.schedule.*
import com.pechuro.bsuirschedule.local.entity.schedule.crossref.*
import com.pechuro.bsuirschedule.local.entity.staff.EmployeeCached
import com.pechuro.bsuirschedule.local.entity.staff.GroupCached

@Database(
        entities = [
            AuditoryCached::class,
            AuditoryTypeCached::class,
            BuildingCached::class,

            DepartmentCached::class,
            EducationFormCached::class,
            FacultyCached::class,
            SpecialityCached::class,

            GroupCached::class,
            EmployeeCached::class,

            EmployeeScheduleExamCached::class,
            EmployeeItemExamCached::class,
            EmployeeScheduleClassesCached::class,
            EmployeeItemClassesCached::class,

            GroupScheduleClassesCached::class,
            GroupItemClassesCached::class,
            GroupScheduleExamCached::class,
            GroupItemExamCached::class,

            EmployeeExamAuditoryCrossRef::class,
            EmployeeExamGroupCrossRef::class,
            EmployeeLessonAuditoryCrossRef::class,
            EmployeeLessonGroupCrossRef::class,

            GroupExamAuditoryCrossRef::class,
            GroupExamEmployeeCrossRef::class,
            GroupLessonAuditoryCrossRef::class,
            GroupLessonEmployeeCrossRef::class
        ],
        exportSchema = false,
        version = BuildConfig.DATABASE_VERSION)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun groupDao(): GroupDao

    abstract fun employeeDao(): EmployeeDao

    abstract fun scheduleDao(): ScheduleDao

    abstract fun buildingDao(): BuildingDao

    abstract fun specialityDao(): SpecialityDao
}