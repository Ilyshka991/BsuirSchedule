package com.pechuro.bsuirschedule.data.mappers

import com.pechuro.bsuirschedule.domain.entity.*
import com.pechuro.bsuirschedule.remote.dto.*

internal fun BuildingDTO.toDomainEntity() = run {
    Building(
            id = id,
            name = name
    )
}

internal fun AuditoryTypeDTO.toDomainEntity() = run {
    AuditoryType(
            id = id,
            name = name,
            abbreviation = abbreviation
    )
}

internal fun DepartmentDTO.toDomainEntity() = run {
    Department(
            id = id,
            name = name,
            abbreviation = abbreviation
    )
}

internal fun AuditoryDTO.toDomainEntity() = run {
    val building = buildingNumber.toDomainEntity()
    val auditoryType = auditoryType.toDomainEntity()
    val department = department?.toDomainEntity()
    Auditory(
            id = id,
            name = name,
            note = note,
            capacity = capacity,
            building = building,
            auditoryType = auditoryType,
            department = department
    )
}

internal fun EmployeeDTO.toDomainEntity() = run {
    Employee(
            id = id,
            firstName = firstName,
            middleName = middleName,
            lastName = lastName,
            abbreviation = abbreviation,
            photoLink = photoLink,
            rank = rank
    )
}

internal fun GroupDTO.toDomainEntity(faculty: Faculty?) = run {
    Group(
            id = id,
            number = number,
            faculty = faculty,
            course = course
    )
}

internal fun AnnouncementDTO.toDomainEntity() = run {
    Announcement(
            date = date,
            content = content,
            employeeName = employeeName
    )
}

internal fun EducationFormDTO.toDomainEntity() = run {
    EducationForm(
            id = id,
            name = name
    )
}

internal fun FacultyDTO.toDomainEntity() = run {
    Faculty(
            id = id,
            name = name,
            abbreviation = abbreviation
    )
}

internal fun SpecialityDTO.toDomainEntity(faculty: Faculty?) = run {
    Speciality(
            id = id,
            name = name,
            abbreviation = abbreviation,
            faculty = faculty,
            educationForm = educationForm.toDomainEntity(),
            code = code
    )
}

internal fun List<ScheduleItemDTO>.toGroupScheduleItems(
        schedule: Schedule.GroupSchedule,
        auditories: List<Auditory>
): List<ScheduleItem.GroupScheduleItem> {
    val resultList = mutableListOf<ScheduleItem.GroupScheduleItem>()
    forEach { scheduleItem ->
        scheduleItem.classes.map { lesson ->
            val lessonAuditories = lesson.auditories?.let { lessonAuditories ->
                auditories.filter { "${it.name}-${it.building.name}" in lessonAuditories }
            }
            val lessonEmployees = lesson.employees?.map { it.toDomainEntity() }
            val mappedScheduleItem = ScheduleItem.GroupScheduleItem(
                    //This ID will be generated later
                    id = 0,
                    schedule = schedule,
                    subject = lesson.subject,
                    weekNumbers = lesson.weekNumber,
                    subgroupNumber = lesson.subgroupNumber,
                    lessonType = lesson.lessonType,
                    auditories = lessonAuditories,
                    note = lesson.note,
                    startTime = lesson.startTime,
                    endTime = lesson.endTime,
                    weekDay = scheduleItem.weekDay,
                    employees = lessonEmployees
            )
            resultList.add(mappedScheduleItem)
        }
    }
    return resultList
}

internal fun List<ScheduleItemDTO>.toEmployeeScheduleItems(
        schedule: Schedule.EmployeeSchedule,
        groups: List<Group>,
        auditories: List<Auditory>
): List<ScheduleItem.EmployeeScheduleItem> {
    val resultList = mutableListOf<ScheduleItem.EmployeeScheduleItem>()
    forEach { scheduleItem ->
        scheduleItem.classes.map { lesson ->
            val lessonAuditories = lesson.auditories?.let { lessonAuditories ->
                auditories.filter { "${it.name}-${it.building.name}" in lessonAuditories }
            }
            val lessonGroups = lesson.studentGroups?.let { lessonGroups ->
                groups.filter { it.number in lessonGroups }
            }
            val mappedScheduleItem = ScheduleItem.EmployeeScheduleItem(
                    //This ID will be generated later
                    id = 0,
                    schedule = schedule,
                    subject = lesson.subject,
                    weekNumbers = lesson.weekNumber,
                    subgroupNumber = lesson.subgroupNumber,
                    lessonType = lesson.lessonType,
                    auditories = lessonAuditories,
                    note = lesson.note,
                    startTime = lesson.startTime,
                    endTime = lesson.endTime,
                    weekDay = scheduleItem.weekDay,
                    studentGroups = lessonGroups
            )
            resultList.add(mappedScheduleItem)
        }
    }
    return resultList
}