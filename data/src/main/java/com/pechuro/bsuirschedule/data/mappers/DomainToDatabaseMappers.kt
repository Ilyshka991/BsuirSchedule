package com.pechuro.bsuirschedule.data.mappers

import com.pechuro.bsuirschedule.domain.entity.*
import com.pechuro.bsuirschedule.local.entity.*

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

internal fun Schedule.GroupSchedule.toDatabaseEntity() = run {
    GroupScheduleCached(
            name = name,
            type = type.value,
            lastUpdate = lastUpdated,
            groupId = group.id
    )
}

internal fun ScheduleItem.GroupScheduleItem.toDatabaseEntity(schedule: GroupScheduleCached) = run {
    val scheduleItem = GroupScheduleItemCached(
            scheduleName = schedule.name,
            scheduleType = schedule.type,
            subject = subject,
            weekNumbers = weekNumbers,
            subgroupNumber = subgroupNumber,
            lessonType = lessonType,
            note = note,
            startTime = startTime,
            endTime = endTime,
            weekDay = weekDay
    )
    val employees = employees?.map { it.toDatabaseEntity() }
    val auditories = auditories?.toDatabaseEntity()
    GroupScheduleItemComplex(
            scheduleItem = scheduleItem,
            employees = employees,
            auditories = auditories
    )
}

internal fun Schedule.EmployeeSchedule.toDatabaseEntity() = run {
    EmployeeScheduleCached(
            name = name,
            type = type.value,
            employeeId = employee.id
    )
}

internal fun ScheduleItem.EmployeeScheduleItem.toDatabaseEntity(schedule: EmployeeScheduleCached) = run {
    val scheduleItem = EmployeeScheduleItemCached(
            scheduleName = schedule.name,
            scheduleType = schedule.type,
            subject = subject,
            weekNumbers = weekNumbers,
            subgroupNumber = subgroupNumber,
            lessonType = lessonType,
            note = note,
            startTime = startTime,
            endTime = endTime,
            weekDay = weekDay
    )
    val groups = studentGroups?.map { it.toDatabaseEntity() }
    val auditories = auditories?.toDatabaseEntity()
    EmployeeScheduleItemComplex(
            scheduleItem = scheduleItem,
            groups = groups,
            auditories = auditories
    )
}