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
import com.pechuro.bsuirschedule.local.entity.schedule.complex.EmployeeClassesItemComplex
import com.pechuro.bsuirschedule.local.entity.schedule.complex.EmployeeExamItemComplex
import com.pechuro.bsuirschedule.local.entity.schedule.complex.GroupClassesItemComplex
import com.pechuro.bsuirschedule.local.entity.schedule.complex.GroupExamItemComplex
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
            rank = rank,
            departmentId = department.id
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
            facultyId = faculty.id,
            course = course,
            specialityId = speciality.id
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
    GroupClassesScheduleCached(
            name = name,
            lastUpdate = lastUpdatedDate,
            groupId = group.id,
            notRemindForUpdates = notRemindForUpdates
    )
}

internal fun Schedule.GroupExams.toDatabaseEntity() = run {
    GroupExamScheduleCached(
            name = name,
            lastUpdate = lastUpdatedDate,
            groupId = group.id,
            notRemindForUpdates = notRemindForUpdates
    )
}


internal fun ScheduleItem.GroupLesson.toDatabaseEntity(schedule: GroupClassesScheduleCached) = run {
    val scheduleItem = GroupItemClassesCached(
            scheduleName = schedule.name,
            subject = subject,
            subgroupNumber = subgroupNumber,
            lessonType = lessonType,
            note = note,
            startTime = startTime,
            endTime = endTime,
            weekDay = weekDay.index,
            weekNumber = weekNumber.index
    )
    val employees = employees.map { it.toDatabaseEntity() }
    val auditories = auditories.toDatabaseEntity()
    GroupClassesItemComplex(
            scheduleItem = scheduleItem,
            employees = employees,
            auditories = auditories
    )
}

internal fun ScheduleItem.GroupExam.toDatabaseEntity(schedule: GroupExamScheduleCached) = run {
    val scheduleItem = GroupItemExamCached(
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
    GroupExamItemComplex(
            scheduleItem = scheduleItem,
            employees = employees,
            auditories = auditories
    )
}

internal fun Schedule.EmployeeClasses.toDatabaseEntity() = run {
    EmployeeClassesScheduleCached(
            name = name,
            employeeId = employee.id
    )
}

internal fun Schedule.EmployeeExams.toDatabaseEntity() = run {
    EmployeeExamScheduleCached(
            name = name,
            employeeId = employee.id
    )
}

internal fun ScheduleItem.EmployeeLesson.toDatabaseEntity(schedule: EmployeeClassesScheduleCached) = run {
    val scheduleItem = EmployeeItemClassesCached(
            scheduleName = schedule.name,
            subject = subject,
            weekNumber = weekNumber.index,
            subgroupNumber = subgroupNumber,
            lessonType = lessonType,
            note = note,
            startTime = startTime,
            endTime = endTime,
            weekDay = weekDay.index
    )
    val groups = studentGroups.map { it.toDatabaseEntity() }
    val auditories = auditories.toDatabaseEntity()
    EmployeeClassesItemComplex(
            scheduleItem = scheduleItem,
            groups = groups,
            auditories = auditories
    )
}

internal fun ScheduleItem.EmployeeExam.toDatabaseEntity(schedule: EmployeeExamScheduleCached) = run {
    val scheduleItem = EmployeeItemExamCached(
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
    EmployeeExamItemComplex(
            scheduleItem = scheduleItem,
            groups = groups,
            auditories = auditories
    )
}