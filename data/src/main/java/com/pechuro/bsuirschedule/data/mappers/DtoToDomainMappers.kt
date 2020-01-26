package com.pechuro.bsuirschedule.data.mappers

import com.pechuro.bsuirschedule.data.utils.getWeekdayFor
import com.pechuro.bsuirschedule.domain.entity.*
import com.pechuro.bsuirschedule.domain.exception.DataSourceException
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

internal fun ScheduleDTO.toDomainEntity(
        scheduleType: ScheduleType,
        lastUpdated: String?,
        groups: List<Group>,
        auditories: List<Auditory>
): Classes = run {
    val scheduleName = when (scheduleType) {
        ScheduleType.STUDENT_CLASSES, ScheduleType.STUDENT_EXAMS ->
            studentGroup?.number
        ScheduleType.EMPLOYEE_CLASSES, ScheduleType.EMPLOYEE_EXAMS ->
            employee?.abbreviation
    } ?: throw DataSourceException.InvalidData
    val schedule = Schedule(
            name = scheduleName,
            type = scheduleType,
            lastUpdated = lastUpdated
    )
    val dtoItemList = when (scheduleType) {
        ScheduleType.STUDENT_CLASSES, ScheduleType.EMPLOYEE_CLASSES -> this.schedule
        ScheduleType.STUDENT_EXAMS, ScheduleType.EMPLOYEE_EXAMS -> this.exam
    } ?: throw DataSourceException.InvalidData
    Classes(
            schedule = schedule,
            items = dtoItemList.toDomainEntity(
                    schedule = schedule,
                    groups = groups,
                    auditories = auditories
            )
    )
}

internal fun List<ScheduleItemDTO>.toDomainEntity(
        schedule: Schedule,
        groups: List<Group>,
        auditories: List<Auditory>
): List<ScheduleItem> {
    val resultList = mutableListOf<ScheduleItem>()
    forEach { scheduleItem ->
        scheduleItem.classes.map { lesson ->
            val lessonAuditories = lesson.auditories?.let { lessonAuditories ->
                auditories.filter { "${it.name}-${it.building.name}" in lessonAuditories }
            }
            val lessonGroups = lesson.studentGroups?.let { lessonGroups ->
                groups.filter { it.number in lessonGroups }
            }
            val lessonEmployees = lesson.employees?.map { it.toDomainEntity() }
            val mappedScheduleItem = ScheduleItem(
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
                    weekDay = getWeekdayFor(scheduleItem.weekDay),
                    employees = lessonEmployees,
                    studentGroups = lessonGroups
            )
            resultList.add(mappedScheduleItem)
        }
    }
    return resultList
}