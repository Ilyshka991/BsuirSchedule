package com.pechuro.bsuirschedule.data.mappers

import com.pechuro.bsuirschedule.domain.entity.*
import com.pechuro.bsuirschedule.local.entity.building.AuditoryCached
import com.pechuro.bsuirschedule.local.entity.building.AuditoryTypeCached
import com.pechuro.bsuirschedule.local.entity.building.BuildingCached
import com.pechuro.bsuirschedule.local.entity.education.DepartmentCached
import com.pechuro.bsuirschedule.local.entity.education.EducationFormCached
import com.pechuro.bsuirschedule.local.entity.education.FacultyCached
import com.pechuro.bsuirschedule.local.entity.education.SpecialityCached
import com.pechuro.bsuirschedule.local.entity.schedule.*
import com.pechuro.bsuirschedule.local.entity.schedule.complex.EmployeeExamComplex
import com.pechuro.bsuirschedule.local.entity.schedule.complex.EmployeeLessonComplex
import com.pechuro.bsuirschedule.local.entity.schedule.complex.GroupExamComplex
import com.pechuro.bsuirschedule.local.entity.schedule.complex.GroupLessonComplex
import com.pechuro.bsuirschedule.local.entity.staff.EmployeeCached
import com.pechuro.bsuirschedule.local.entity.staff.GroupCached

internal fun Department.toDatabaseEntity() = run {
    DepartmentCached(
            id = id,
            name = name,
            abbreviation = abbreviation
    )
}

internal fun AuditoryType.toDatabaseEntity() = run {
    AuditoryTypeCached(
            id = id,
            name = name,
            abbreviation = abbreviation
    )
}

internal fun Building.toDatabaseEntity() = run {
    BuildingCached(
            id = id,
            name = name
    )
}

internal fun Auditory.toDatabaseEntity(
        building: BuildingCached,
        auditoryType: AuditoryTypeCached,
        department: DepartmentCached?
) = run {
    AuditoryCached(
            id = id,
            name = name,
            note = note,
            capacity = capacity,
            buildingId = building.id,
            auditoryTypeId = auditoryType.id,
            departmentId = department?.id
    )
}

internal fun List<Auditory>.toDatabaseEntity() = map {
    val auditoryTypeCached = it.auditoryType.toDatabaseEntity()
    val departmentCached = it.department?.toDatabaseEntity()
    val buildingCached = it.building.toDatabaseEntity()
    it.toDatabaseEntity(
            auditoryType = auditoryTypeCached,
            building = buildingCached,
            department = departmentCached
    )
}

internal fun Employee.toDatabaseEntity() = run {
    EmployeeCached(
            id = id,
            firstName = firstName,
            middleName = middleName,
            lastName = lastName,
            abbreviation = abbreviation,
            photoLink = photoLink,
            rank = rank
    )
}

internal fun EducationForm.toDatabaseEntity() = run {
    EducationFormCached(
            id = id,
            name = name
    )
}

internal fun Group.toDatabaseEntity() = run {
    GroupCached(
            id = id,
            number = number,
            facultyId = faculty?.id,
            course = course
    )
}

internal fun Speciality.toDatabaseEntity() = run {
    SpecialityCached(
            id = id,
            name = name,
            facultyId = faculty?.id,
            educationFormId = educationForm.id,
            abbreviation = abbreviation,
            code = code
    )
}

internal fun Faculty.toDatabaseEntity() = run {
    FacultyCached(
            id = id,
            abbreviation = abbreviation,
            name = name
    )
}

internal fun Schedule.GroupClasses.toDatabaseEntity() = run {
    GroupScheduleClassesCached(
            name = name,
            lastUpdate = lastUpdated,
            groupId = group.id
    )
}

internal fun Schedule.GroupExams.toDatabaseEntity() = run {
    GroupScheduleExamsCached(
            name = name,
            lastUpdate = lastUpdated,
            groupId = group.id
    )
}


internal fun ScheduleItem.GroupLesson.toDatabaseEntity(schedule: GroupScheduleClassesCached) = run {
    val scheduleItem = GroupLessonCached(
            scheduleName = schedule.name,
            subject = subject,
            subgroupNumber = subgroupNumber,
            lessonType = lessonType,
            note = note,
            startTime = startTime,
            endTime = endTime,
            weekDay = weekDay.index,
            weekNumbers = weekNumbers
    )
    val employees = employees.map { it.toDatabaseEntity() }
    val auditories = auditories.toDatabaseEntity()
    GroupLessonComplex(
            scheduleItem = scheduleItem,
            employees = employees,
            auditories = auditories
    )
}

internal fun ScheduleItem.GroupExam.toDatabaseEntity(schedule: GroupScheduleExamsCached) = run {
    val scheduleItem = GroupExamCached(
            scheduleName = schedule.name,
            subject = subject,
            subgroupNumber = subgroupNumber,
            lessonType = lessonType,
            note = note,
            startTime = startTime,
            endTime = endTime,
            date = date
    )
    val employees = employees.map { it.toDatabaseEntity() }
    val auditories = auditories.toDatabaseEntity()
    GroupExamComplex(
            scheduleItem = scheduleItem,
            employees = employees,
            auditories = auditories
    )
}

internal fun Schedule.EmployeeClasses.toDatabaseEntity() = run {
    EmployeeScheduleClassesCached(
            name = name,
            employeeId = employee.id
    )
}

internal fun Schedule.EmployeeExams.toDatabaseEntity() = run {
    EmployeeScheduleExamsCached(
            name = name,
            employeeId = employee.id
    )
}

internal fun ScheduleItem.EmployeeLesson.toDatabaseEntity(schedule: EmployeeScheduleClassesCached) = run {
    val scheduleItem = EmployeeLessonCached(
            scheduleName = schedule.name,
            subject = subject,
            weekNumbers = weekNumbers,
            subgroupNumber = subgroupNumber,
            lessonType = lessonType,
            note = note,
            startTime = startTime,
            endTime = endTime,
            weekDay = weekDay.index
    )
    val groups = studentGroups.map { it.toDatabaseEntity() }
    val auditories = auditories.toDatabaseEntity()
    EmployeeLessonComplex(
            scheduleItem = scheduleItem,
            groups = groups,
            auditories = auditories
    )
}

internal fun ScheduleItem.EmployeeExam.toDatabaseEntity(schedule: EmployeeScheduleExamsCached) = run {
    val scheduleItem = EmployeeExamCached(
            scheduleName = schedule.name,
            subject = subject,
            subgroupNumber = subgroupNumber,
            lessonType = lessonType,
            note = note,
            startTime = startTime,
            endTime = endTime,
            date = date
    )
    val groups = studentGroups.map { it.toDatabaseEntity() }
    val auditories = auditories.toDatabaseEntity()
    EmployeeExamComplex(
            scheduleItem = scheduleItem,
            groups = groups,
            auditories = auditories
    )
}