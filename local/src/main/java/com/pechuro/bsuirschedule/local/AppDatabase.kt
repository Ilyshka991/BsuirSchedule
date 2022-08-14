package com.pechuro.bsuirschedule.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pechuro.bsuirschedule.local.converters.DateConverter
import com.pechuro.bsuirschedule.local.dao.BuildingDao
import com.pechuro.bsuirschedule.local.dao.EmployeeDao
import com.pechuro.bsuirschedule.local.dao.GroupDao
import com.pechuro.bsuirschedule.local.dao.ScheduleDao
import com.pechuro.bsuirschedule.local.dao.SpecialityDao
import com.pechuro.bsuirschedule.local.entity.building.AuditoryCached
import com.pechuro.bsuirschedule.local.entity.building.AuditoryTypeCached
import com.pechuro.bsuirschedule.local.entity.building.BuildingCached
import com.pechuro.bsuirschedule.local.entity.education.DepartmentCached
import com.pechuro.bsuirschedule.local.entity.education.EducationFormCached
import com.pechuro.bsuirschedule.local.entity.education.FacultyCached
import com.pechuro.bsuirschedule.local.entity.education.SpecialityCached
import com.pechuro.bsuirschedule.local.entity.schedule.EmployeeClassesScheduleCached
import com.pechuro.bsuirschedule.local.entity.schedule.EmployeeExamScheduleCached
import com.pechuro.bsuirschedule.local.entity.schedule.EmployeeItemClassesCached
import com.pechuro.bsuirschedule.local.entity.schedule.EmployeeItemExamCached
import com.pechuro.bsuirschedule.local.entity.schedule.GroupClassesScheduleCached
import com.pechuro.bsuirschedule.local.entity.schedule.GroupExamScheduleCached
import com.pechuro.bsuirschedule.local.entity.schedule.GroupItemClassesCached
import com.pechuro.bsuirschedule.local.entity.schedule.GroupItemExamCached
import com.pechuro.bsuirschedule.local.entity.schedule.crossref.EmployeeExamAuditoryCrossRef
import com.pechuro.bsuirschedule.local.entity.schedule.crossref.EmployeeExamGroupCrossRef
import com.pechuro.bsuirschedule.local.entity.schedule.crossref.EmployeeLessonAuditoryCrossRef
import com.pechuro.bsuirschedule.local.entity.schedule.crossref.EmployeeLessonGroupCrossRef
import com.pechuro.bsuirschedule.local.entity.schedule.crossref.GroupExamAuditoryCrossRef
import com.pechuro.bsuirschedule.local.entity.schedule.crossref.GroupExamEmployeeCrossRef
import com.pechuro.bsuirschedule.local.entity.schedule.crossref.GroupLessonAuditoryCrossRef
import com.pechuro.bsuirschedule.local.entity.schedule.crossref.GroupLessonEmployeeCrossRef
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

        EmployeeExamScheduleCached::class,
        EmployeeItemExamCached::class,
        EmployeeClassesScheduleCached::class,
        EmployeeItemClassesCached::class,

        GroupClassesScheduleCached::class,
        GroupItemClassesCached::class,
        GroupExamScheduleCached::class,
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
    version = BuildConfig.DATABASE_VERSION
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun groupDao(): GroupDao

    abstract fun employeeDao(): EmployeeDao

    abstract fun scheduleDao(): ScheduleDao

    abstract fun buildingDao(): BuildingDao

    abstract fun specialityDao(): SpecialityDao
}